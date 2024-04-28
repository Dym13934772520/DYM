<template>
	<view>
		
		
	
		
		<!-- 导航栏 -->
		<view style="display: flex;align-items: center;padding: 0 30rpx;">
			<view :class="tabIndex === index ? 'actives' : ''" v-for="(item,index) in tabBar" :key="index" style="margin-right: 20rpx;padding: 10rpx 30rpx;border-radius: 30rpx;font-size: 30rpx;" @click="changeIndex(index)">{{item.name}}</view>
		</view>
		
		<!-- 内容区域 -->
		<view class="" style="margin: 10px 15px 20px 25px;">
			<view style="height: 150rpx; margin-top: 84rpx; margin-bottom: 190px;" v-for="(item3,index3) in typeDetail" :key="index3" v-if="(tabIndex === 0) || (tabIndex === 1 && item3.status === 0) || (tabIndex === 2 && item3.status === 1) || (tabIndex === 3 && item3.status === 2)">
				<view style="font-size: 32rpx;color: #2e3034;display: flex;">Number：{{item3.id}} </view>
				<view style="font-size: 32rpx;color: #2e3034;display: flex;">{{item3.status == 1 ? 'Customer add order' : item3.status == 2 ? 'Deliver man accepted' : 'Customer get'}}</view>
				<view style="font-size: 32rpx;color: #B7BFCE;display: flex;">Time：{{happenTimeFun(item3.createdAt)}}</view>
				<view style="font-size: 32rpx;color: #B7BFCE;display: flex;">Address：{{item3.address}}</view>
				<view style="font-size: 32rpx;color: #B7BFCE;display: flex;">Phone：{{item3.phone}}</view>
			
			<view style="display: flex;align-items: center;margin-top: 12px;gap: 15px;flex-wrap: wrap;">
				<view v-for="(v,i) in item3.orderDetailsList" :key="i" style="display: flex;align-items: center;flex-direction: column;">
					<image :src="v.imgUrl && v.imgUrl.split(',')[v.imgUrl.split(',').length-1]" mode="" style="width: 120rpx;height: 120rpx;margin-right: 20rpx;margin-bottom: 10px;"></image>
					<view class="" style="flex:1;">
						<view style="font-size: 28rpx;color: #333;font-weight: 550;" class="lineTe">{{v.goodsName}}</view>
						<view class="" style="width: 100%">
							
							<view style="color: rgb(255, 76, 44);text-align: center;">￡ {{v.sellingPrice}}</view>
							<view style="height: 30%; display: flex;flex-direction: column; ">
								
								
							</view>
						</view>
					</view>
				</view>
			</view>
				<view style="height: 80rpx;width: 100%;display: flex;align-items: center; gap: 10px;margin-top: 10px;">
					<!-- <template>
						<view style="height: 70%; font-size: 30rpx;color: black;display: flex;justify-content: flex-end;font-weight: 550;" v-if="item3.status == 0">待付款</view>
						<view style="height: 70%; font-size: 30rpx;color: black;display: flex;justify-content: flex-end;font-weight: 550;" v-else-if="item3.status == 1">待发货</view>
						<view style="height: 70%; font-size: 30rpx;color: black;display: flex;justify-content: flex-end;font-weight: 550;" v-else-if="item3.status == 2">已完成</view>
					</template> -->
					<!-- <view style="height: 30%; display: flex;flex-direction: column; ">
						<view style="font-size: 14rpx;color: #B7BFCE;display: flex;">订单编号：{{item3.id}}</view>
						<view style="font-size: 14rpx;color: #B7BFCE;display: flex;">下单时间：{{happenTimeFun(item3.createdAt)}}</view>
					</view> -->
					<view style="background-color:rgb(40, 217, 52);color: #fff;padding: 10px;text-align: center;font-size: 14px;" @click="aldim(item3.id)" v-if="item3.status == 2">Get</view>
					<view style="background-color:rgb(255, 76, 44);color: #fff;padding: 10px;text-align: center;font-size: 14px;" @click="dlteebutton(item3.id)">Cancel</view>
				</view>
			</view>
			<view>
			<view style="font-size: 32rpx;color: #B7BFCE;display: flex;"> </view>	
			</view>	
		</view>
	
		
		
		
	</view>
</template>

<script>

	export default {
		
		data() {
			return {
				tabIndex:0,
				tabBar:[
					{name:'All'},
					
				],
				typeDetail:[]
			}
		},
		onLoad() {
			this.getTypeDetail()
		},
		methods: {
			backTo(){
				uni.navigateBack({
					delta:1
				})
			},
			changeIndex(index){
				if (this.tabIndex === index) {
					return;
				}
				this.tabIndex = index;
			},
			getTypeDetail(){
				uni.request({
					url:this.$elyasApi+'order/findByModal',
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json",
						"accessToken":uni.getStorageSync('token')
					},
					success:(data)=> {
						this.typeDetail = data.data.data.list;
					}
					
				});
			},
			aldim(id){
				uni.request({
					url:this.$elyasApi+'order/updateGoods',
					method:'POST',
					data:{id:id,status:3},
					header:{
						"content-type":"application/json",
						"accessToken":uni.getStorageSync('token')
					},
					success:(data)=> {
						this.getTypeDetail()
					}
					
				});
			},
			dlteebutton(id) {
				uni.request({
					url:this.$elyasApi+'order/delete?id='+id,
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json",
						"accessToken":uni.getStorageSync('token')
					},
					success:(data)=> {
						this.getTypeDetail()
					}
					
				});
			},
			happenTimeFun(num){//时间戳数据处理
						
			let date = new Date(num);
			
			//时间戳为10位需*1000，时间戳为13位的话不需乘1000
			
			        let y = date.getFullYear();
			
			        let MM = date.getMonth() + 1;
			
			        MM = MM < 10 ? ('0' + MM) : MM;//月补0
			
			        let d = date.getDate();
			
			        d = d < 10 ? ('0' + d) : d;//天补0
			
			      
			        return y + '-' + MM + '-' + d;
			
			},
			
		}
	}
</script>

<style>
	.actives {
		background-color: rgb(33, 118, 255) !important;
		color: #fff !important;
	}
	
	.lineTe{
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 1;
		-webkit-box-orient: vertical;
	}
	
	
</style>
