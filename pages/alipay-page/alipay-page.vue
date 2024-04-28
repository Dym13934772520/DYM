<template>
	<view class="out-side-box">
		<!-- 订单顶部信息 -->
		<!-- <view>
			<view style="color: rgb(25, 25, 25);font-size: 28rpx;margin-bottom: 10rpx;">订单编号：687493338271</view>
			<view style="color: rgb(102, 112, 133);font-size: 26rpx;">下单时间：2022年9月3日</view>
		</view> -->
		
		<!-- 流程线 -->
		<view style="margin-top: 42rpx;display: flex;align-items: center;justify-content: space-between;">
			<view style="">
				<image src="/static/icons/select-yes.png" mode="" style="width: 48rpx;height: 48rpx;"></image>
				<view style="font-size: 22rpx;text-align: center;color: rgb(33, 118, 255);">Add order</view>
			</view>
			
			<view style="">
				<image src="/static/icons/select-yes.png" mode="" style="width: 48rpx;height: 48rpx;"></image>
				<view style="font-size: 22rpx;text-align: center;">Payment</view>
			</view>
			
		
			
			<view style="">
				<image src="" mode="" style="width: 48rpx;height: 48rpx;"></image>
				<view style="font-size: 22rpx;text-align: center;">Finish</view>
			</view>
		</view> 
		
		<!-- 下单商品列表 -->
		<view style="margin-top: 60rpx;">
			
			
			<view style="display: flex;align-items: center;justify-content: space-between;margin-bottom: 65rpx;" v-for="(item,index) in goodsList" :key="index">
				<view class="" style="width:94rpx;height:118rpx;margin-right: 20rpx;">
					<image :src="item.imgUrl && item.imgUrl.split(',')[item.imgUrl.split(',').length-1]" mode="" style="width: 100%;height: 100%;"></image>
				</view>
				<view style="flex: 1;">
					<view style="font-size: 28rpx;color: rgb(13, 17, 27);">{{item.goodsName}}</view>
					<view style="color: rgb(183, 191, 206);font-size: 24rpx;margin-top: 2rpx;"></view>
					<template v-if="item.sellingPrice > 0">
						<view style="color: rgb(255, 76, 44);font-weight: 550;margin-top: 10rpx;">{{'￡'+item.sellingPrice}}</view>
					</template>
					<template v-else>
						<view style="color: rgb(33, 118, 255);font-weight: 550;margin-top: 10rpx;">Free</view>
					</template>
				</view>
				
			</view>
	
			
			
		</view>
		
		<!-- 支付方式 -->
		<view style="margin-top: 40rpx;">
			
			<view style="display: flex;align-items: center;justify-content: space-between;">
				<view style="font-size: 30rpx;color: #333;font-weight: 550;">Payment method</view>
			</view>
			
			<view style="margin-top: 30rpx;display: flex;align-items: center;justify-content: space-between;">
				<view style="display: flex;align-items: center;">
					<image src="/static/icons/ali-pay-icon.png" mode="" style="width: 80rpx;height: 80rpx;margin-right: 34rpx;"></image>
					<view style="font-size: 30rpx;color: #000;">Alipay</view>
				</view>
				
				<image src="/static/icons/select-yes.png" mode="" style="width: 48rpx;height: 48rpx;"></image>
			</view>
			
		</view>
		
		<!-- 提交订单 -->
		<view class="footer-buttons">
			<view style="color: rgb(255, 76, 44);font-weight: 550;">￡{{getTotal.sellingPrice}}</view>
			<view style="display: flex;align-items: center;">
				<button style="font-size: 26rpx;color: #fff;background-color:rgb(33, 118, 255) ;border-radius: 30px;width: 180rpx;height: 55rpx;line-height: 55rpx;" @click="towait">Pay now！</button>
			</view>
		</view>
		
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				goodsList:[],
			}
		},
		onLoad(option) {
			this.goodsList = JSON.parse(decodeURIComponent(option.selectItems));
		},
		computed:{
			getTotal(){
				var number = 0;
				var sellingPrice = 0;
				for(var i=0; i < this.goodsList.length;i++){
					if (this.goodsList[i].cartFlag === true) {
						number += this.goodsList[i].number;
						sellingPrice += this.goodsList[i].number * this.goodsList[i].sellingPrice
					}
				}
				
				return {sellingPrice:sellingPrice,number:number}
			}
		},
		methods: {
			// 选择商品
			selectItem(item){
				item.cartFlag = !item.cartFlag
			},
			towait(){
				uni.navigateTo({
					url:'../done-order/done-order?selectItems='+encodeURIComponent(JSON.stringify(this.goodsList.filter(item => item.cartFlag)))
				})
			},
		}
	}
</script>

<style lang="scss">
	.out-side-box{
		padding: 30rpx 60rpx;
	}
	
	.footer-buttons{
		position: absolute;
		bottom: 0;
		left: 0;
		height: 110rpx;
		width: 750rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 50rpx;
	}
</style>
