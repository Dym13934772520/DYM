<template>
	<view>
		
	
		   
		<!-- 选项卡 -->
		
		<view class="" style="margin-top: 60rpx;display: flex;align-items: center;justify-content: space-between;padding: 0 30rpx;" @click="toEditUser">
			<view class="" style="display: flex;align-items: center;">
				<view class="">Change data</view>
			</view>
			
			<image src="/static/icons/right.png" mode="" style="width: 14rpx;height: 26rpx;"></image>
		</view>
		
	
		
		<view class="" style="padding: 0 30rpx;margin-top: 600rpx;font-size: 28rpx;">
			<button style="background-color: rgb(33, 118, 255);color: #fff;border-radius: 10rpx;"  @click="outLogin">Logout</button>
		</view>
		
	</view>
</template>

<script>

	export default {
	
		
		data() {
			return {
				
			}
		},
		methods: {
			outLogin(){
				console.log('1232');
				uni.setStorageSync('token','')
				
				setTimeout(function(){
					uni.navigateTo({
						url:'/pages/account-login/account-login'
					})
				},500)
				
			},
			backTo(){
				uni.navigateBack({
					delta:1
				})
			},
			toEditUser(){
				uni.navigateTo({
					url:'../setUser/setUser'
				})
			},
			
		}
	}
</script>

<style>

</style>
