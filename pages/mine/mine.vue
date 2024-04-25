<template>
	<view style="padding: 0 30rpx;">
		<!-- 顶部适配方案 -->

		
		<view style="display: flex;align-items: center;justify-content:space-between;">
			<view></view>
			<view style="font-size: 35rpx;color: #333;font-weight: 550;">Individual center</view>
			<view @click="toSetting">
				<image src="/static/icons/settings-o 设置-线.png" mode="" style="width: 44rpx;height: 44rpx;"></image>
			</view>
		</view>
		
			<!-- 5,numurluk,may,ikki,7,may,ikki,yokungning,xijejing,birtal,aqqiksu,ikki,halta,kahwa,alta,ozangga,naqqini,alisan,,xobiy,yilaguarlik,bax -->
		
		<!-- 头像 name -->
		<view style="display:flex;justify-content:center;width: 100%;margin-top: 60rpx;">
			<view class="" style="width:148rpx;" @click="toEditUser">
				<image :src="userInfo.avatar && userInfo.avatar.split(',')[userInfo.avatar.split(',').length-1]" mode="" style="width:148rpx;height: 148rpx;border-radius: 100%;"></image>
				<view style="font-size: 34rpx;color: #333;margin-top: 10rpx;text-align: center;">{{userInfo.userName}}</view>
			</view>
		</view>
		
		<!-- 金币 -->
		<!-- <view class="" style="height: 130rpx;background-color:rgb(33, 118, 255);border-top-left-radius: 45rpx;border-top-right-radius: 45rpx;margin-top: 50rpx;display: flex;align-items: center;justify-content: space-between;padding: 0 30rpx;">
			 <view style="display: flex;align-items: center;">
				 <image src="/static/icons/金币,硬币,货币,Coin.png" mode="" style="width: 82rpx;height: 82rpx;margin-right: 15rpx;"></image>
				 <view class="" style="font-size: 40rpx;color: #fff;">{{userInfo.score}} 积分</view>
			 </view>
			 
			 <view class="" style="background-color: #fff;color: rgb(33, 118, 255);font-size: 25rpx;padding:15rpx 35rpx;border-radius: 30rpx;" @click="toscoreClass">兑换课程</view>
		</view> -->
		
		<!-- 选项卡 -->
		
		<view class="" style="margin-top: 60rpx;display: flex;align-items: center;justify-content: space-between;" @click="tomymy">
			<view class="" style="display: flex;align-items: center;">
				<!-- <image src="/static/icons/问答区.png" mode="" style="width: 64rpx;height: 64rpx;margin-right: 15rpx;"></image> -->
				<view class="">Personal data</view>
			</view>
			
			<image src="/static/icons/right.png" mode="" style="width: 14rpx;height: 26rpx;"></image>
		</view>
		
		<view class="" style="margin-top: 60rpx;display: flex;align-items: center;justify-content: space-between;" @click="toMyOrder" v-if="userInfo.roleId == 1">
			<view class="" style="display: flex;align-items: center;">
				<!-- <image src="/static/icons/我的订单.png" mode="" style="width: 64rpx;height: 64rpx;margin-right: 15rpx;"></image> -->
				<view class="">My order</view>
			</view>
			
			<image src="/static/icons/right.png" mode="" style="width: 14rpx;height: 26rpx;"></image>
		</view>
		
		
		<view class="" style="margin-top: 60rpx;display: flex;align-items: center;justify-content: space-between;" @click="openshangjia" v-if="userInfo.roleId == 1">
			<view class="" style="display: flex;align-items: center;">
				<!-- <image src="/static/icons/我的订单.png" mode="" style="width: 64rpx;height: 64rpx;margin-right: 15rpx;"></image> -->
				<view class="">Contact platform</view>
			</view>
			
			<image src="/static/icons/right.png" mode="" style="width: 14rpx;height: 26rpx;"></image>
		</view>
		
		
		<view class="" style="margin-top: 60rpx;display: flex;align-items: center;justify-content: space-between;" @click="toliuyan" v-if="userInfo.roleId == 1">
			<view class="" style="display: flex;align-items: center;">
				<!-- <image src="/static/icons/我的订单.png" mode="" style="width: 64rpx;height: 64rpx;margin-right: 15rpx;"></image> -->
				<view class="">Message board</view>
			</view>
			
			<image src="/static/icons/right.png" mode="" style="width: 14rpx;height: 26rpx;"></image>
		</view>
		
		<view class="" style="margin-top: 60rpx;display: flex;align-items: center;justify-content: space-between;" @click="toliuyan2" v-if="userInfo.roleId == 1">
			<view class="" style="display: flex;align-items: center;">
				<!-- <image src="/static/icons/我的订单.png" mode="" style="width: 64rpx;height: 64rpx;margin-right: 15rpx;"></image> -->
				<view class="">My message</view>
			</view>
			
			<image src="/static/icons/right.png" mode="" style="width: 14rpx;height: 26rpx;"></image>
		</view>
		<view class="" style="margin-top: 60rpx;display: flex;align-items: center;justify-content: space-between;" @click="historys" v-if="userInfo.roleId == 3">
			<view class="" style="display: flex;align-items: center;">
				<!-- <image src="/static/icons/我的订单.png" mode="" style="width: 64rpx;height: 64rpx;margin-right: 15rpx;"></image> -->
				<view class="">Historical order</view>
			</view>
			
			<image src="/static/icons/right.png" mode="" style="width: 14rpx;height: 26rpx;"></image>
		</view>
		
		<view class="" style="margin-top: 60rpx;display: flex;align-items: center;justify-content: space-between;" @click="toCarthandle" v-if="userInfo.roleId == 1">
			<view class="" style="display: flex;align-items: center;">
				<!-- <image src="/static/icons/我的订单.png" mode="" style="width: 64rpx;height: 64rpx;margin-right: 15rpx;"></image> -->
				<view class="">Shopping cart</view>
			</view>
			
			<image src="/static/icons/right.png" mode="" style="width: 14rpx;height: 26rpx;"></image>
		</view>
		
	
		<u-modal v-model="show" :content="content"></u-modal>
		
		
		
	</view>
</template>

<script>

	export default {

		
		data() {
			return {
				userInfo:{},
				show:false,
				content:'13899801450'
			}
		},
		onShow() {
			this.getUserInfoByToken()
		},
		methods: {
			toCarthandle(){
				uni.navigateTo({
					url:'/pages/cart/cart'
				})
			},
			toSetting(){
				uni.navigateTo({
					url:'../setting-page/setting-page'
				})
			},
			toMyOrder(){
				uni.navigateTo({
					url:'../my-order/my-order'
				})
			},
			toMySave(){
				uni.navigateTo({
					url:'../my-save/my-save'
				})
			},
			myScore(){
				uni.navigateTo({
					url:'../scoreList/scoreList'
				})
			},
			historys(){
				uni.navigateTo({
					url:'/pages/history/history'
				})
			},
			getUserInfoByToken(){
				uni.request({
					url:this.$elyasApi+'user/detailByToken',
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json",
						"accessToken":uni.getStorageSync('token')
					},
					success:(data)=> {
						this.userInfo = data.data.data;
					}
					
				});
			},
			toCart(){
				uni.navigateTo({
					url:'../cart/cart'
				})
			},
			toYijian(){
				uni.navigateTo({
					url:'../liuyan/liuyan'
				})
			},
			toscoreClass(){
				uni.navigateTo({
					url:'../scoreClass/scoreClass'
				})
			},
			tomymy(){
				uni.navigateTo({
					url:'../setUser/setUser'
				})
				
			},
			toliuyan(){
				uni.navigateTo({
					url:'../liuyan/liuyan'
				})
			},
			openshangjia(){
				this.show = true
			},
			toliuyan2(){
				uni.navigateTo({
					url:'../mymessage/mymessage'
				})
			},
			jiedan(){
				uni.navigateTo({
					url:'/pages/jiedanzi/jiedanzi'
				})
			}
		}
	}
</script>

<style>

</style>
