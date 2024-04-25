<template>
	<view class="out-side-box">
		<!-- 顶部适配方案 -->
	
		
		<!-- 顶部 -->
		<view style="display: flex;align-items: center;justify-content: space-between;">
		
			          
			<!-- <view>
				<image :src="userInfo.avatar && userInfo.avatar.split(',')[userInfo.avatar.split(',').length-1]" mode="" style="height: 68rpx;width: 68rpx;border-radius: 100%;"></image>
				
				<view style="color: rgb(102, 112, 133);font-size: 20rpx;">{{userInfo.userName}}</view>
			</view>	 -->
		</view>
		
		<button style="background-color: rgb(33, 118, 255);height: 68rpx;line-height: 68rpx;color: #fff;font-size: 26rpx;width:638rpx;margin-top: 64rpx;margin-bottom: 54rpx;">Cart</button>
		
		<!-- 购物车商品列表 -->
		<view style="padding: 30rpx;padding-bottom: 100rpx;">
			
			<view style="display: flex;align-items: center;justify-content: space-between;margin-bottom: 65rpx;" v-for="(item,index) in cartList" :key="item.id">
				<view class="" style="width:94rpx;height:118rpx;margin-right: 20rpx;">
					<image :src="item.imgUrl && item.imgUrl.split(',')[item.imgUrl.split(',').length-1]" mode="" style="width: 100%;height: 100%;"></image>
				</view>
				<view style="flex: 1;">
					<view style="font-size: 28rpx;color: rgb(13, 17, 27);">{{item.goodsName}}</view>
				
					<template v-if="item.sellingPrice > 0">
						<view style="color: rgb(255, 76, 44);font-weight: 550;margin-top: 10rpx;">{{'￡'+item.sellingPrice}}</view>
					</template>
					<template >
						<view style="color: rgb(164, 159, 163);font-weight: 550;margin-top: 10rpx;">{{'Number：'+item.number}}</view>
					</template>
				</view>
				<view class="">
					<label for="" @click="selectItem(item)">
						<image src="/static/icons/select-no.png" mode="" style="width: 82rpx;height: 82rpx;" :dataSolid="cartFlag" v-if="item.cartFlag === false"></image>
						<image src="/static/icons/select-yes.png" mode="" style="width: 62rpx;height: 62rpx;" :dataSolid="cartFlag" v-else></image>
					</label>
					<view style="display:flex;align-items: center;" @click="deleteBtn(item.cartId)">
						<image src="/static/icons/delete-icon.png" mode="" style="width: 26rpx;height: 26rpx;margin-right: 10rpx;"></image>
						<view style="font-size: 24rpx;color: rgb(183, 191, 206);" :dataSolid="cartFlag" v-if="item.cartFlag === true">Delete</view>
					</view>
				</view>
			</view>
			
		</view>
		
		<!-- 结算 -->
		<view style="padding: 0 30rpx;" class="footer-buttons">
			<view style="display: flex;align-items: center;">
					<view style="font-size: 28rpx;margin-right: 100rpx;">Selected<text style="color: rgb(33, 118, 255);">{{getTotal.number}}</text>Goods，Total：<text style="color: rgb(255, 76, 44);">{{getTotal.sellingPrice}}</text></view>
					<button style="background-color: rgb(33, 118, 255);color: #fff;font-size: 13px;width: 83px;height: 27px;text-align: center;line-height: 27px;border-radius: 30px;" @click="toInput">GO to result</button>
			</view>
		</view>
			
	</view>
</template>
<script>
	
	export default {
	
		data() {
			return {
				userInfo:{},
				cartList:[],
				totalMoney:0
			}
		},
		onLoad() {
			this.getCartData()
			this.getUserInfoByToken()
		},
		onShow() {
			this.getCartData()
		},
		created() {
			this.getCartData()
		},
		computed:{
			// 选中总价计算
			getTotal(){
				var number = 0;
				var sellingPrice = 0;
				for(var i=0; i < this.cartList.length;i++){
					if (this.cartList[i].cartFlag === true) {
						number += this.cartList[i].number;
						sellingPrice += this.cartList[i].number * this.cartList[i].sellingPrice
					}
				}
				
				return {sellingPrice:sellingPrice,number:number}
			}
		},
		methods: {
			backTo(){
				uni.navigateBack({
					delta:1
				})
			},
			getCartData(){
				uni.request({
					url:this.$elyasApi+'cart/findByModal',
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json",
						"accessToken":uni.getStorageSync('token')
					},
					success:(data)=> {
						this.cartList = data.data.data.list;
					}
					
				});
			},
			// 选择商品
			selectItem(item){
				item.cartFlag = !item.cartFlag
			},
			// 带参数跳转结算页面
			toInput(){
				uni.navigateTo({
					url:'../input-order/input-order?selectItems='+encodeURIComponent(JSON.stringify(this.cartList.filter(item => item.cartFlag)))
				})
			},
			getUserInfoByToken(){
				uni.request({
					url:this.$elyasApi+'user/detailByToken',
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json",
						"accessToken":this.$store.state.userInfo.token
					},
					success:(data)=> {
						this.userInfo = data.data.data;
					}
					
				});
			},
			// 删除购物车内容
			deleteBtn(id){
				uni.request({
					url:this.$elyasApi+'cart/delete?id='+id,
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json",
						"accessToken":uni.getStorageSync('token')
					},
					success:(data)=> {
						console.log(data.data.code);
						if (data.data.code !== 200) {
							uni.showToast({
								title:'Deletion failure'
							})
							return;
						}else {
							uni.showToast({
								title:'Successfully deleted！'
							})
							this.getCartData()
						}
					}
					
				});
			}
		}
	}
</script>

<style lang="scss">
	
	.out-side-box{
		padding: 0 38rpx;
	}
	
	.footer-buttons{
		// position: fixed;
		// bottom: 0;
		// left: 0;
		// background-color: aqua;
		background-color: #fff;
		height: 110rpx;
		width: 750rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 50rpx;
	}

</style>
