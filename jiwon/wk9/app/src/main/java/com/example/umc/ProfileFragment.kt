package com.example.umc

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.umc.databinding.FragmentProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var followingUsers by mutableStateOf<List<ReqResUser>>(emptyList())
    private var hasRequestedMyPage = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFollowingPagerContent()
    }

    private fun setFollowingPagerContent() {
        binding.composeFollowingPager.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LaunchedEffect(Unit) {
                    if (!hasRequestedMyPage) {
                        hasRequestedMyPage = true
                        loadMyPageFromReqRes()
                    }
                }

                FollowingHorizontalPager(users = followingUsers)
            }
        }
    }

    private suspend fun loadMyPageFromReqRes() {
        showLoadingState()

        runCatching {
            withContext(Dispatchers.IO) {
                requestMyPageData()
            }
        }.onSuccess { result ->
            bindSuccessState(result)
        }.onFailure { throwable ->
            bindFailureState(throwable)
        }
    }

    private suspend fun requestMyPageData(): MyPageNetworkResult {
        var lastErrorMessage = "알 수 없는 오류"

        RetrofitClient.apiKeys.forEach { apiKey ->
            try {
                val myProfileResponse = RetrofitClient.api.getUser(userId = 1, apiKey = apiKey)
                val followingResponse = RetrofitClient.api.getUsers(page = 1, apiKey = apiKey)

                val myProfileBody = myProfileResponse.body()
                val followingBody = followingResponse.body()

                if (myProfileResponse.isSuccessful && followingResponse.isSuccessful && myProfileBody != null && followingBody != null) {
                    return MyPageNetworkResult(
                        myProfile = myProfileBody.data,
                        followingUsers = followingBody.data,
                        userHttpCode = myProfileResponse.code(),
                        followingHttpCode = followingResponse.code()
                    )
                }

                lastErrorMessage = "HTTP ${myProfileResponse.code()} / ${followingResponse.code()}"
            } catch (exception: Exception) {
                lastErrorMessage = exception.message ?: exception.javaClass.simpleName
            }
        }

        throw IllegalStateException("ReqRes API 호출 실패: $lastErrorMessage")
    }

    private fun showLoadingState() {
        followingUsers = emptyList()
        binding.progressProfile.isVisible = true
        binding.tvProfileError.isVisible = false
        binding.tvMyNickname.text = "회원 정보를 불러오는 중"
        binding.tvMyEmail.text = "ReqRes API 연결 중입니다"
        binding.tvApiStatus.text = "NETWORK LOADING"
        binding.tvApiStatus.setTextColor(Color.parseColor("#757575"))
        binding.tvFollowerCount.text = "0"
        binding.tvFollowingCount.text = "0"
    }

    private fun bindSuccessState(result: MyPageNetworkResult) {
        val user = result.myProfile
        val users = result.followingUsers

        binding.progressProfile.isVisible = false
        binding.tvProfileError.isVisible = false

        binding.tvMyNickname.text = user.nickname
        binding.tvMyEmail.text = user.email
        binding.tvApiStatus.text = "ReqRes API 연동 성공 · userId=${user.id} · HTTP ${result.userHttpCode}/${result.followingHttpCode}"
        binding.tvApiStatus.setTextColor(Color.parseColor("#2E7D32"))

        binding.tvPostCount.text = "12"
        binding.tvFollowerCount.text = users.size.toString()
        binding.tvFollowingCount.text = users.size.toString()

        Glide.with(this)
            .load(user.avatar)
            .circleCrop()
            .placeholder(R.drawable.user)
            .error(R.drawable.user)
            .into(binding.ivMyProfile)

        followingUsers = users
    }

    private fun bindFailureState(throwable: Throwable) {
        followingUsers = emptyList()
        binding.progressProfile.isVisible = false
        binding.tvProfileError.isVisible = true
        binding.tvProfileError.text = throwable.message ?: "ReqRes API 호출에 실패했습니다."

        binding.tvMyNickname.text = "API 연동 실패"
        binding.tvMyEmail.text = "서버에서 회원 정보를 받아오지 못했습니다"
        binding.tvApiStatus.text = "NETWORK FAIL"
        binding.tvApiStatus.setTextColor(Color.parseColor("#C62828"))

        binding.tvFollowerCount.text = "0"
        binding.tvFollowingCount.text = "0"
        binding.ivMyProfile.setImageResource(R.drawable.user)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
