<template>
	<view>
		
		<view class="Ck">
			<image :src="shopData.image && shopData.image.split(',')[shopData.image.split(',').length-1]" mode=""></image>
			<view class="">
				<view class="kkc" style="margin-bottom: 5px;">
					<view class="title">{{shopData.name}}</view>
					<view class="title">Shop：{{shopData.userName}}</view>
				</view>	
				
				<view class="" v-html="shopData.address"></view>
				<view class="">Shop Phone：{{shopData.phone}}</view>
			</view>
		</view>	
		
		<!-- 数据展示内容 -->
		<view class="cate">
			<view class="cateLeft">
				<scroll-view class="cateNameList" scroll-y="true">
					<view class="cateNameItem" :class="{catActive:index == current}" v-for="(item,index) in goodsTypeList" :key="index" @click="menuTab(index,item.id)">
						{{item.goodsType}}
						<view class="catActiveLine"></view>
					</view>
					
				</scroll-view>
				
			</view>
			
			<view class="cateRight">
				
				<view class="cateRightList">
					<!-- <view class="search">
						<view class="searchBox" style="display: flex;align-items: center;">
							<input type="text" style="padding: 4px;border-radius: 10px;flex:1;border-bottom: 1px solid #999;">
							<button type="normal" size="mini">搜索</button>
						</view>
					</view> -->
					
					
					<view class="cateRightItem" v-for="(item3,index3) in goodsList" :key="index3" @click="toGoodsDetail(item3.id)" >
						<image :src="item3.imgUrl && item3.imgUrl.split(',')[item3.imgUrl.split(',').length-1]" mode="aspectFill" ></image>
						<text style="margin-top: 10px;">{{item3.goodsName}}</text>
						<view style="margin-top: 10px;color: #DD524D;font-weight: 700;display: flex;align-items: center;">
							<text style="font-size: 30rpx;">￡ {{item3.sellingPrice}}</text>
							<text style="color: #999;font-weight: 500;">{{item3.repertory}} </text>
						</view>
					</view>
					
				
				</view>
				
				
				
				
			</view>
		</view>
				
	</view>
</template>

<script>
	export default {
		data() {
			return {
				goodsTypeList:[],
				current:0,
				selectGoods:[],
				goodstypeid:110,
				shoplist:{},
				goodsList:[],
				shopId:'',
				shopData:{},
				params:{
					shopId:'',
					goodsType:''
				}
			}
		},
		onLoad(option) {
			this.shopId = option.id
			this.params.shopId = option.id
			this.getShopDetail(option.id)
		
			
			this.getgoodsTypeList()
				this.getGoods()
			
		},
		methods: {
			getShopDetail(id){
				uni.request({
					url:this.$elyasApi+'shop/detail?id='+id,
					method:'POST',
					data:{},
					header:{
					    "content-type":"application/json"
					},
					success:(data)=> {
						this.shopData = data.data.data
						
						
					}
					
				})
			},
			getGoods(){
				uni.request({
					url:this.$elyasApi+'goods/findByModal',
					method:'POST',
					data:this.params,
					header:{
					    "content-type":"application/json"
					},
					success:(data)=> {
						
						this.goodsList =[]
						
						data.data.data.list.forEach(item=>{
							if (item.repertory >= 1) {
								this.goodsList.push(item)
							}
						})
						
						
					}
					
				})
			},
			getgoodsTypeList(){
				uni.request({
					url:this.$elyasApi+'goods/type/findAllByModal',
					method:'POST',
					data:{},
					header:{
					    "content-type":"application/json"
					},
					success:(data)=> {
						// data.data.data.forEach(item =>{
						
						// 	if (item.goodsList.length > 0) {
								
						// 		this.goodsTypeList.push(item)
						// 	}
						// })
						
						this.goodsTypeList = data.data.data.list
						this.goodsTypeList.unshift({goodsType:'All',id:''})
						
						
					}
					
				})
			},
			menuTab(index,id){
				this.current = index
				console.log(id)
				this.params.goodsType = id
				this.getGoods()
					
			},
			// 获取矩形信息
			getRectInfo(){
				let view = uni.createSelectorQuery().in(this).select("#cate0")
				
				view.fields({
					size:true,
					scrollOffset:true
				}, data =>{
					console.log("Get node information"+JSON.stringify(data));//得到节点信息 节点的宽为
					console.log("The width of the node is"+ data.width);
				}).exec();
			},
			toGoodsDetail(id){
				uni.navigateTo({
					url:'../goods-detail-buy/goods-detail-buy?id='+id
				})
			}
		}
	}
</script>

<style scoped>
	page{background-color: #f7f7f7;}
	.search{height: 110rpx;width: 100%;background-color: #fff;display: flex;justify-content: center;align-items: center;}
	.searchBox{width: 690rpx;height: 70rpx;border-radius: 10rpx;color: #999;font-size: 28rpx;background-color: #f7f7f7;text-align: center;line-height: 70rpx;}
	
	.cate{position: absolute;top: 110px;bottom: 0;width: 100%;background-color: #fff;display: flex;}
	.cateLeft{width: 200rpx;background-color: #f7f7f7;height: 100%;}
	.cateNameList{height: 100%;overflow: auto;}
	.cateNameItem{font-size: 28rpx;color: #000;line-height: 100rpx;text-align: center;position:relative ;}
	.catActiveLine{position: absolute;width: 4rpx;height: 30rpx;background-color: #f7f7f7;top: 35rpx;}
	.catActive{background-color: #fff;}
	
	/* 点击之后颜色改变 */
	.catActive .catActiveLine{background-color: #DD524D;}
	.cateRight{width: 550rpx;background-color: #fff;height: 100%;}
	.cateRightList{height: auto;overflow: hidden;}
	.cateRightScroll{height: 100%;overflow:auto;}
	
	.cateRightItem{width: 33.33%;float: left;margin-top: 20rpx;}
	.cateRightItem image{width: 160rpx;height: 160rpx;display: block;margin: 0 auto;}
	.cateRightItem text{line-height: 36rpx;font-size: 24rpx;text-align: center;display: block;width: 100%;overflow: hidden;text-overflow: ellipsis;white-space:nowrap;}
	
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
	
</style>
