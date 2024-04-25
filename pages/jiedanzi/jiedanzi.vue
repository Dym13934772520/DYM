<template>
	<view>

		
	
		
		<!-- 内容区域 -->
		<view class="" style="padding: 0 30rpx;">
			<view style="height: 150rpx; margin-top: 84rpx; margin-bottom: -50rpx;display: flex;align-items: center;" v-for="(item3,index3) in typeDetail" :key="index3" v-if="(tabIndex === 0) || (tabIndex === 1 && item3.status === 0) || (tabIndex === 2 && item3.status === 1) || (tabIndex === 3 && item3.status === 2)">
				<image :src="item3.orderDetailsList[0].imgUrl && item3.orderDetailsList[0].imgUrl.split(',')[item3.orderDetailsList[0].imgUrl.split(',').length-1]" mode="" style="width: 120rpx;height: 120rpx;margin-right: 20rpx;"></image>
				<view class="" style="flex:1;">
					<view style="font-size: 28rpx;color: #333;font-weight: 550;" class="lineTe">{{item3.goodsName}}</view>
					<view class="" style="width: 100%">
						
						<view style="color: rgb(255, 76, 44);">{{item3.sellingPrice}}</view>
						<view style="height: 30%; display: flex;flex-direction: column; ">
							<view style="font-size: 22rpx;color: #B7BFCE;display: flex;">Number：{{item3.id}}</view>
							<view style="font-size: 22rpx;color: #B7BFCE;display: flex;">Order time：{{happenTimeFun(item3.createdAt)}}</view>
							<view style="font-size: 24rpx;color: #ce2a1e;display: flex;margin-top: 5px;">{{item3.status == 1 ? 'Wait for order' : item3.status == 2 ? 'Accepted the order' : 'Customer get'}}</view>
						</view>
					</view>
				</view>
				<view style="height: 80rpx;width: 25%;display: flex;flex-direction: column; justify-content: flex-end;">
					
					<view style="background-color:rgb(40, 217, 52);color: #fff;padding: 10px;text-align: center;font-size: 14px;" @click="dlteebutton(item3.id)" v-if="item3.status == 1">Set Order</view>
					<view style="background-color:rgb(207, 216, 217);color: #fff;padding: 10px;text-align: center;font-size: 14px;"  v-if="item3.status == 2">Accepted the order</view>
				</view>
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
			dlteebutton(id) {
				uni.request({
					url:this.$elyasApi+'order/updateGoods',
					method:'POST',
					data:{id:id,status:2},
					header:{
						"content-type":"application/json",
						"accessToken":uni.getStorageSync('token')
					},
					success:(data)=> {
						uni.showToast({
							title:'Order accepted successfully',
						});
						
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
