<template>
	<view>
		<u-swiper :list="goodsInfo.imgUrl.split(',')"  duration="3000" height="400"></u-swiper>
		<view class="title-box">
			<title style="font-size:16px;color: #333;font-weight: 550;">{{goodsInfo.goodsName}}</title>
			
			<view style="display: flex;align-items: center;justify-content: space-between;margin-top: 8px;">
				<view style="color: rgb(33, 118, 255);font-size: 10px;">
					<u-number-box v-model="value" @change="valChange" :min="1" :max="goodsInfo.repertory" disabled-input="false"></u-number-box>
				</view>
				<view style="color: rgb(255, 76, 44);font-weight: 550;">
					￡{{goodsInfo.sellingPrice}}
				</view>
			</view> 
		</view>
		
		<!-- 师资介绍 -->
		<view  style="margin-top: 40rpx;">
			<view style="width: 750rpx;height: 60rpx;border-bottom: 0.5px solid rgb(167, 167, 167);">
				<text style="line-height: 60rpx;margin-left: 10px;font-size: 13px;font-weight: 550;">Goods details</text>
			</view>
				
			<view class="u-content" style="padding: 10px;">
					<u-parse :html="goodsInfo.graphicDetails"></u-parse>
				</view>
		</view>
		
		<view class="footer-buttons">
			<view style="color: rgb(255, 76, 44);font-weight: 550;">￡{{goodsInfo.sellingPrice * number}}</view>
			<view style="display: flex;align-items: center;">
				<button style="font-size: 26rpx;color: #fff;background-color:rgb(33, 118, 255) ;border-radius: 30px;margin-right: 20rpx;width: 250rpx;height: 55rpx;line-height: 55rpx;" @click="toBuyCart">Add to cart</button>
				<!-- <button style="font-size: 26rpx;color: #fff;background-color:rgb(33, 118, 255) ;border-radius: 30px;width: 180rpx;height: 55rpx;line-height: 55rpx;">立即支付</button> -->
			</view>
		</view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				goodsInfo:{},
				goodsId:'',
				value:1,
				number:1,
				shopId:'',
				params:{
					shopAddress:'',
					shopPhone:'',
					goodsId:'',
					number:''
				}
			}
		},
		created() {
			console.log(this.$store.state.userInfo,'user')
		},
		onLoad(option) {
			console.log(option.id,'option')
			this.goodsId = option.id;
			this.params.goodsId = option.id;
			this.getGooodsDetail();
			
			
		},
		methods: {
			toBuyCart(){
				uni.request({
					url:this.$elyasApi+'cart/add',
					method:'POST',
					data:this.params,
					header:{
						"content-type":"application/json",
						"accessToken":uni.getStorageSync('token')
					},
					success:(data)=> {
						uni.showToast({
							title:'Successful！'
						})
						
						setTimeout(()=>{
							uni.navigateTo({
								url:'../cart/cart'
							})
						},400)
					}
					
				});
			},
			valChange(e) {
				console.log('当前值为: ' + e.value)
				this.number = e.value
				this.params.number = e.value
			},
			getGooodsDetail(){
				uni.request({
					url:this.$elyasApi+'goods/detail?id='+this.goodsId,
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json",
						"accessToken":this.$store.state.userInfo.token
					},
					success:(data)=> {
						this.goodsInfo = data.data.data;
						setTimeout(()=>{
							uni.request({
								url:this.$elyasApi+'shop/detail?id='+data.data.data.shopId,
								method:'POST',
								data:{},
								header:{
									"content-type":"application/json",
									"accessToken":this.$store.state.userInfo.token
								},
								success:(data)=> {
									this.params.shopAddress = data.data.data.address
									this.params.shopPhone = data.data.data.phone
								}
								
							});
						},500)
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

<style lang="scss">
	.title-box{
		width: 750rpx;
		height: 196rpx;
		border-bottom: 0.5px solid rgb(159, 159, 159);
		border-bottom-left-radius: 40rpx;
		border-bottom-right-radius: 40rpx;
		padding: 10px 10px;
		box-shadow: 10rpx 8rpx 7rpx 5rpx #9F9F9F;
	}
	
	.teacher-list {
		padding: 30rpx;
		.teacher-item{
			display: flex;
			justify-content: center;
			// align-items: flex-start;
			margin-bottom:80rpx;
			background: #f1f6ff;
			border-radius: 20rpx;
			padding: 4% 0;
		}
	}
	
	.footer-buttons{
		position: fixed;
		bottom: 0;
		left: 0;
		height: 110rpx;
		width: 750rpx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		background-color: #fff;
		padding: 50rpx;
	}
</style>
