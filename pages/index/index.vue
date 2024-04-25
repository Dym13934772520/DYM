<template>
	<view class="home-box">
		<view class="users" v-if="userData.roleId == 1">
			
				<view class="search">
					<view class="searchContetn">
						<input type="text" placeholder="Search" placeholder-class="plz" />
						<button>Search</button>
					</view>
				</view>
				
				<view class="shoplist">
					<view class="sitem" v-for="(item,index) in shoplist" :key="index" @click="toShopItem(item.id)">
						<image :src="item.image" mode=""></image>
						<view class="sinfo">
							<view class="name">{{item.name}}</view>
							<view class="f1">
								<view class="f1left">
									<view class="score">5.0⭐</view>
									<view class="mounthSell">Monthly sale300+</view>
								</view>
								<view class="f1right">
									31minutes 1.9km
								</view>
							</view>
							<view class="f2">
								<view class="f2left">
									<view class="score">Start￡20</view>
									<view class="mounthSell">Free delivery over 30</view>
								</view>
							
							</view>
							<view class="labels">
								<view class="label">
									35-3|48-7
								</view>
								<view class="label">
									169-20
								</view>								
							</view>
						</view>
					</view>
				</view>
				
				
			
		</view>
			
			
			<view class="qishou" v-else>
				<view class="statuslist">
					<view :class="optIndex == item.id ? 'opt active' : 'opt'" v-for="item in optlist" :key="item.id" @click="selectOPT(item.id)">{{item.name}}</view>
					
				</view>	
				<view class="boxview">
					<view class="gabarItem" v-for="(item,index) in typeData" :key="index" v-if="(optIndex == 1 && item.status == 1) || (optIndex == 2 && item.status == 2)">
						<view class="status yes" style="width: 100%;background-color: #f6f6f6;color: #333;">Number：{{item.id}}</view>
						<view style="display: flex;align-items: center;gap: 5px;">
							<view class="status no">{{item.status == 1 ? 'Placed an order' : 'Accepted the order'}}</view>
							<!-- 骑手接单页面-->
							<view class="status" @click="jie(item.id)" style="background-color: darkcyan;" v-if="item.status == 1">Get order</view>
							
						</view>
						<view style="margin: 10px 0;font-size: 14px;">
							<view style="margin-bottom: 10px;">Consignee phone/address:{{item.phone}} {{item.address}}</view>
							<view>Business phone/address:{{item.orderDetailsList[0].shopPhone}} {{item.orderDetailsList[0].shopAddress}}</view>
						</view>
						<view class="iteminfo" v-for="(v,i) in item.orderDetailsList" :key="i">
							<image :src="v.imgUrl ? v.imgUrl.split(',')[0] : v.imgUrl" mode="aspectFill"></image>
							<view class="textareaa">
								<view class="namai">{{v.goodsName}}</view>
								<view class="namai">￡ {{v.sellingPrice}}</view>
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
				optlist:[
					{name:'Wait for order',id:1},
					{name:'Get order',id:2},
				],
				optIndex:1,
				typeID:'',
				typeDetail:[],
				tabBarIndex:0,
				scrollIntoIndex:'top0',
				// 内容高度
				contentHeight:0,
				tabBar:[],
				goodsData:[],
				yearNum:0,
				show:false,
				imagelist:[],
				shoplist:{},
				typeData:[],
				userData:{}
				
			}
		},
		onLoad(){
			// this.getTypeData()
			// this.getGoodsData()
			// console.log(this.$store.state.loginStatus,'loginStatus');
			// // this.SocketOnline()
			// this.getimage()
			this.getshop()
			
			this.getOrdersData()
		
		},
		onShow() {
			// this.SocketOnline()
			// this.$store.dispatch('openSoket')
			this.getUserDatas()
		},
		onReady(){
			// 获取高度
			
			// let view = uni.createSelectorQuery().select('.home-element');
			// view.boundingClientRect(data =>{
			// 	console.log(data.height)
			// 	this.contentHeight = data.height + 1400;
			// }).exec();
			
			
		},
		methods: {
			selectOPT(id){
				this.optIndex = id
			},
			toSearch(){
				uni.navigateTo({
					url:'../searchpage/searchpage'
				})
			},
			toShopItem(id){
				uni.navigateTo({
					url:'/pages/type/type?id='+id
				})
			},
			confirm(e) {
				console.log(e[0].value,'eeeeeeee');
				this.yearNum = e[0].value;
				this.yearName = e[0].label;
				this.getGoodsData()
			},
			// 获取分类数据
			getTypeData(){
				uni.request({
					url:this.$elyasApi+'goods/type/findByModal',
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json"
					},
					success:(data)=> {
						this.tabBar = data.data.data.list;
						this.tabBar.unshift({goodsType:'推荐',index:0});
					}
					
				});
			},
			getshop(){
				uni.request({
					url:this.$elyasApi+'shop/findAllByModal',
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json"
					},
					success:(data)=> {
						this.shoplist = data.data.data.list;
						
					}
					
				});
			},
			// 获取分类详情
			getTypeDetail(){
				uni.request({
					url:this.$elyasApi+'goods/type/detail?id='+this.typeID,
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json"
					},
					success:(data)=> {
						this.typeDetail = data.data.data.goodsList;
					}
					
				});
			},
			// 获取公开课列表
			getGoodsData(){
				uni.request({
					url:this.$elyasApi+'goods/findByModal',
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json"
					},
					success:(data)=> {
						this.goodsData = data.data.data.list;
					}
					
				});
			},
			getOrdersData(){
				uni.request({
					url:this.$elyasApi+'order/findByModal',
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json",
						"accessToken":uni.getStorageSync('token')
					},
					success:(data)=> {
						this.typeData = data.data.data.list;
					}
					
				});
			},
			jie(id){
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
							title:'Received order',
						});
						this.getOrdersData()
					}
					
				});
			},
			getUserDatas(){
				uni.request({
					url:this.$elyasApi+'user/detailByToken',
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json",
						"accessToken":uni.getStorageSync('token')
					},
					success:(data)=> {
						this.userData = data.data.data;
						if (data.data.data.roleId != 1) {
						
						}
					}
					
				});
			},
			getimage(){
				uni.request({
					url:this.$elyasApi+'cover/findByModal',
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json"
					},
					success:(data)=> {
						this.imagelist = data.data.data.list;
					}
					
				});
			},
			toTab(){
				uni.navigateTo({
					url:'../classList/classList'
				})
			},
			toArticlePage(){
				this.checkAuth(()=>{
					uni.navigateTo({
						url:'../article-page/article-page'
					})
				})
				
			},
			// 点击切换tab
			changeIndex(index,itemId){
				if (this.tabBarIndex === index){
					return;
				}
				this.tabBarIndex = index;
				this.scrollIntoIndex = 'top'+index;
				// console.log(itemId,'item id');
				this.typeID = itemId;
				this.getTypeDetail()
			},
			onChangeSwiper(e){
				this.changeIndex(e.detail.current);
				
			},
			 // 免费课程详情
			toGoodsDetail(id){
				uni.navigateTo({
					url:'../goods-detail-buy/goods-detail-buy?id='+id
				})
			},
			// 收费课程详情
			toGoodsDetailBuy(id){
				uni.navigateTo({
					url:'../goods-detail-buy/goods-detail-buy?id='+id
				})
			},
			// 测试 跳转登录页
			toLogin(){
				uni.navigateTo({
					// url:'../login/login'
				})
			},
			test(){
				uni.navigateTo({
					url:'../cart/cart'
				})
			},
			toHelp(){
				uni.navigateTo({
					url:'../help-page/help-page'
				})
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
			// socket online
			SocketOnline(){	
				uni.request({
					url:this.$elyasApi+'socket/join?userName='+this.$store.state.userInfo.userName,
					method:'GET',
					data:{},
					header:{
						"content-type":"application/json"
					},
					success:(data)=> {
						console.log(data)
					}
					
				});
			},
			openShopDetail(id){
				uni.request({
					url:this.$elyasApi+'shop/detail?id='+id,
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json"
					},
					success:(data)=> {
						console.log(data)
					}
					
				});
			},
			toDabao(){
				uni.switchTab({
					url:'../buylist/buylist'
				})
				
			}
		}
	}
</script>

<style lang="scss" scoped>
	.lineTe{
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 1;
		-webkit-box-orient: vertical;
	}
	.home-box{
		// padding: 0 30rpx;
		background-color: #f6f6f6;
		height: 100%;
	}
	
	
	
	.buttontwo{
		display: flex;
		align-items: center;
		justify-content: space-between;
		
	}
	
	.buttontwo button {
		flex: 1;
		margin: 10px 0;
		padding: 20px 0;
		color: #fff;
		font-weight: 700;
		font-size: 20px;
	}
	
	.buttontwo button:nth-child(1){
		
		
		
		border-radius: 10px;
		background: linear-gradient(145deg, #f6d365, #fda085);
		box-shadow:  6px 6px 8px #fda085,
		             -6px -6px 8px #f6d365;
	}
	
	.buttonc{
		// background-color: #FBAB7E;
		// background-image: linear-gradient(62deg, #FBAB7E 0%, #F7CE68 100%);
border-radius: 10px;
background: linear-gradient(145deg, #F7CE68, #FBAB7E);
box-shadow:  6px 6px 8px #FBAB7E,
             -6px -6px 8px #F7CE68;
	}
	
	.shopitem {
		margin: 10px 0;
		display: flex;
		flex-wrap: wrap;
		justify-content: center;
	}
	
	.shopitem image {
		width: 170px;
		height: 170px;
		border-radius: 10px;
		margin: 1px;
		
	}
	
	.shopitem .textbox {
		flex: 1;
	}
	
	.shopitem .textbox .title{
		font-size: 18px;
		font-weight: 600;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 1;
		-webkit-box-orient: vertical;
	}
	
	
	
	.textbox .content{
		font-size: 12px;
		font-weight: 500;
		color: #999;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 4;
		-webkit-box-orient: vertical;
	}
		
		
		.search {
			width: 100%;
			display: flex;
			justify-content: center;
			padding: 5px;
			background-color: #fff;
			margin: 0;
			height: 60px;
			.searchContetn {
				width: 100%;
				border: 2px solid #3f58fb;
				display: flex;
				align-items: center;
				border-radius: 30px;
				height: 40px;
				// line-height: 45px;
				
				input{
					flex: 1;
					padding-left: 1em;
				}
				button {
					width: 80px;
					font-size: 15px;
					border-radius: 30px;
					background-color: #3f58fb;
					color: #fff;
					height: 33px;
					lighting-color: 33px;
					margin-right: 2px;
				}
			}
		}
	
	.plz {
		font-size: 12px;
	}
	
	.shoplist {
		padding: 5px;
		
		.sitem {
			padding: 10px 6px;
			display: flex;
			background-color: #fff;
			border-radius: 12px;
			margin-bottom: 8px;
			 box-shadow:0 0 10px #00000015;
			image {
				width: 125px;
				height: 125px;
				border-radius: 12px;
				object-fit: cover;
				margin-right: 5px;
				
			}
			
			.sinfo {
				flex: 1;
				.name {
					font-size: 17px;
					font-weight: 700;
					width: 100%;
					    overflow: hidden;
					    text-overflow: ellipsis;
					    display: -webkit-box;
					    -webkit-line-clamp: 1;
					    -webkit-box-orient: vertical;
						margin-bottom: 10px;
				}
				.f1 {
					display: flex;
					align-items: center;
					justify-content: space-between;
					font-size: 14px;
					margin-bottom: 12px;
					.f1left {
						display: flex;
						align-items: center;
						.score {
							margin-right: 6px;
							color: #fb621b;
							font-size: 15px;
							font-weight: 700;
						}
						
						.mounthSell {
							color: #999;
						}
					}
					
					.f1right {
						color: #999;
					}
				}
				
				.f2 {
					display: flex;
					align-items: center;
					justify-content: space-between;
					margin-bottom: 15px;
					.f2left {
						display: flex;
						align-items: center;
						
						.score {
							margin-right: 8px;
							color: #fb621b;
							font-size: 13px;
							// font-weight: 700;
						}
						
						.mounthSell {
							color: #999;
						}
					}
				}
				
				.labels {
					display: flex;
					align-items: center;
					font-size: 11px;
					color: #fb621b;
					gap: 5px;
					.label {
						border: 1px solid #fb621b;
						padding: 5px 3px;
					}
				}
			}
		}
	}
	
	.boxview {
		display: flex;
		flex-wrap: wrap;
		gap: 15px;
		margin-top: 15px;
		background-color: #fff;
		padding: 10px;
		box-shadow:0 0 10px #00000025;
		.boxitem {
			width: 130px;
			height: 130px;
			background-color: #55aaff;
			border-radius: 15px;
			color: #fff;
			display: flex;
			flex-direction: column;align-items: center;
			justify-content: center;
			box-shadow:0 0 10px #00000025;
			gap: 10px;
			image {
				width: 60px;
				height: 60px;
				border-radius: 100%;
			}
		}
		
		.boxitem:nth-child(1){
			background-color: #ffd013;
		}
		.boxitem:nth-child(2){
			background-color: #59d836;
		}
		.boxitem:nth-child(3){
			background-color: #3288b6;
		}
		.boxitem:nth-child(4){
			background-color: #ff641c;
		}
	}
	
	.gabarItem {
		width: 100%;
		.status {
			width: 130px;
			height: 25px;
			color: #fff;
			text-align: center;
			line-height: 25px;
			
			&.no {
				background-color: #f4c60b;
			}
			
			&.yes {
				background-color: #f4361c;
			}
		}
		
		.iteminfo {
			padding: 10px;
			border-top: 1px solid #ddd;
			border-bottom: 1px solid #ddd;
			display: flex;
			align-items: center;
			
			image {
				width: 80px;
				height: 80px;
				
			}
			
			.textareaa {
				flex: 1;
				margin-left: 12px;
				font-size: 14px;
				color: #444;
				view{
					margin-bottom: 5px;
				}
			}
		}
	}
	
	.statuslist {
		display: flex;
		align-items: center;
		width: 100%;
		
		.opt {
			flex: 1;
			padding: 10px;
			text-align: center;
			background-color: #fff;
			
			&.active {
				background-color: #4e9cea;
				color: #fff;
			}
		}
	}
	
</style>
