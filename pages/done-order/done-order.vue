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
				<view style="font-size: 22rpx;text-align: center;color: rgb(33, 118, 255);">Order</view>
			</view>
			
			<view style="">
				<image src="/static/icons/select-yes.png" mode="" style="width: 48rpx;height: 48rpx;"></image>
				<view style="font-size: 22rpx;text-align: center;">Pay</view>
			</view>
			
	
			
			<view style="">
				<image src="/static/icons/select-yes.png" mode="" style="width: 48rpx;height: 48rpx;"></image>
				<view style="font-size: 22rpx;text-align: center;">Finish</view>
			</view>
		</view> 
		
		<!-- 下单商品列表 -->
		<view style="margin-top: 60rpx;">
			
			<view class="Ck" style="margin-bottom: 20px;">
				
				<view class="">
					<view class="kkc" style="margin-bottom: 5px;">
						<view class="title">{{shoplist.name}}</view>
						
					</view>	
					
					<view class="" v-html="shoplist.address"></view>
				</view>
			</view>	
			
			
			<view style="display: flex;align-items: center;justify-content: space-between;margin-bottom: 65rpx;" v-for="(item,index) in goodsList" :key="index">
				<view class="" style="width:94rpx;height:118rpx;margin-right: 20rpx;">
					<image :src="item.imgUrl && item.imgUrl.split(',')[item.imgUrl.split(',').length-1]" mode="" style="width: 100%;height: 100%;"></image>
				</view>
				<view style="flex: 1;">
					<view style="font-size: 28rpx;color: rgb(13, 17, 27);">{{item.goodsName}}</view>
				
					<template v-if="item.sellingPrice > 0">
						<view style="color: rgb(255, 76, 44);font-weight: 550;margin-top: 10rpx;">{{'￡'+item.sellingPrice}}</view>
					</template>
					<template v-else>
						<view style="color: rgb(33, 118, 255);font-weight: 550;margin-top: 10rpx;">free</view>
					</template>
				</view>
			<!-- 	<view class="">
					<label for="" @click="selectItem(item)">
						<image src="/static/icons/select-yes.png" mode="" style="width: 82rpx;height: 82rpx;" :dataSolid="cartFlag" v-if="item.cartFlag === false"></image>
						<image src="/static/icons/select-no.png" mode="" style="width: 62rpx;height: 62rpx;" :dataSolid="cartFlag" v-else></image>
					</label>
					<view style="display:flex;align-items: center;">
						<image src="/static/icons/delete-icon.png" mode="" style="width: 26rpx;height: 26rpx;margin-right: 10rpx;"></image>
						<view style="font-size: 24rpx;color: rgb(183, 191, 206);">删除</view>
					</view>
				</view> -->
			</view>
			
			<textarea name="" id="" cols="15" rows="10" style="border: 1px solid #2176ff;padding: 10px;" placeholder="Please enter remarks" v-model="remark"></textarea>
			
			<input class="inputss" type="text" value="" placeholder="Consignee's address" v-model="address" />
			<input class="inputss" type="text" value="" placeholder="Consignee's mobile phone number" v-model="phone" />
		</view>
		
		
	<!-- 	<view>
			<view style="display: flex;align-items: center;margin-bottom: 30rpx;"><text style="color: coral;margin-right: 20rpx;">*</text> <text>选择方式</text></view>
			<view class="" style="display: flex;align-items: center;flex-wrap: wrap;">
				<view :class="selectIndex === index ? 'select-active' : ''" style="font-size: 30rpx;border: 1px solid #000;text-align: center;line-height: 50rpx;border-radius: 30rpx;margin: 10rpx 20rpx;padding:0 30rpx;" v-for="(item,index) in typeList" :key="index" @click="changeIndex(index,item.name)">{{item.name}}</view>	
			</view>	
		</view> -->
		
		
		<!-- 提交订单 -->
		<view class="footer-buttons">
			<button style="font-size: 40rpx;color: #fff;background-color:rgb(33, 118, 255) ;border-radius: 30px;width: 565rpx;height: 88rpx;line-height: 88rpx;" @click="toGoodsDetailBuyBack">Finish</button>
		</view>
		
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				selectIndex:0,
				goodsList:[],
				orderDetailsList:[],
				shoplist:{},
				soul:'',
				typeList:[
					{
						name:'Takeout'
					},
					{
						name:'Eat in'
					}
				],
				address:'',
				phone:'',
				remark:''
			}
		},
		onLoad(option) {
			this.goodsList = JSON.parse(decodeURIComponent(option.selectItems));
			console.log(JSON.parse(decodeURIComponent(option.selectItems)),'222222');
			
			let resultObj = JSON.parse(decodeURIComponent(option.selectItems))
			
			console.log(resultObj,'resultObj')
			
			resultObj.forEach(item=>{
				this.orderDetailsList.push({
					goodsId:item.id,
					price:item.sellingPrice,
					shopAddress:item.shopAddress,
					shopPhone:item.shopPhone,
					number:item.number,
				})
			})
			
			this.getshop()
			
		},
		methods: {
			// 选择商品
			selectItem(item){
				console.log(item.cartFlag,'22222222');
				item.cartFlag = !item.cartFlag
				if (item.cartFlag === false) {
					this.orderDetailsList.push({
						goodsId:item.id,
						price:item.sellingPrice,
						shopAddress:item.shopAddress,
						shopPhone:item.shopPhone,
						number:1,
					})
				}
				
				
			},
			changeIndex(index,itemId){
				if (this.selectIndex === index) {
					return;
				}
				this.selectIndex = index;
				this.soul = itemId
			},
			getshop(){
				uni.request({
					url:this.$elyasApi+'shop/findByModal',
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json"
					},
					success:(data)=> {
						this.shoplist = data.data.data.list[0];
						
					}
					
				});
			},
			toGoodsDetailBuyBack(){
				
				if (this.phone == '') {
					uni.showToast({
						title:'The phone number cannot be empty！'
					});
					return;
				}
				
				if (this.address == '') {
					uni.showToast({
						title:'The address cannot be empty！'
					});
					return;
				}
				
				if (this.remark == '') {
					uni.showToast({
						title:'The remarks cannot be empty！'
					});
					return;
				}
				
				uni.request({
					url:this.$elyasApi+'order/add',
					method:'POST',
					data:{orderDetailsList:this.orderDetailsList,status:0,soul:this.soul,phone:this.phone,address:this.address},
					header:{
						"content-type":"application/json",
						"accessToken":uni.getStorageSync('token')
					},
					success:(data)=> {
						console.log(data.data.data,'datadatadatadatadatadata');
						
						if (data.data.code !== 200) {
							uni.showToast({
								title:data.data.message
							})
							return;
						}else {
							uni.showToast({
								title:'Order successful！'
							})
							
							setTimeout(function(){
								uni.switchTab({
									url:'../index/index'
								})
							},500)
						}
					}
 					
				});
				
				
				
			
				
				
			}
		}
	}
</script>

<style lang="scss">
	.inputss{
		border: 1px solid #ddd;
		width: 100%;
		margin: 10px 0;
		padding: 5px;
	}
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
		background-color: #fff;
	}
	
	.Ck {
		padding: 0 10px;
		display: flex;
		
	}
	.Ck image {
		width: 100px;
		height: 100px;
		object-fit: cover;
		margin-right: 10px;
		border-radius: 5px;
	}
	.Ck .kkc{
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	
	.Ck .kkc .title {
		flex: 1;
		font-weight: 700;
	}
	
	.Ck .kkc .buttons {
		display: flex;
		align-items: center;
		font-size: 14px;
	}
	
	.Ck .kkc .buttons view:nth-child(1) {
		margin-right: 10px;
	}
	
	.select-active{
		color: coral;
		border: 1px solid coral !important;
	}
</style>
