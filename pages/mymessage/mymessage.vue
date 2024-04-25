<template>
	<view style="padding: 20rpx 30rpx;">
		
		<view style="margin-bottom: 80rpx;" v-for="(item,index) in score_List" :key="index" >
			<view style="color: #fff;width: auto;padding: 10rpx 20rpx;">
				<text style="background-color: #333;padding: 10rpx 20rpx;">{{item.name}}</text>
			</view>
			<view class="" style="display: flex;align-items: center;justify-content: space-between;padding-left: 20rpx;margin-top: 20rpx;color: #444;font-weight: 550;">
				<text style="min-width: 20rpx;overflow: hidden;text-overflow: ellipsis;display: -webkit-box;-webkit-line-clamp: 1;-webkit-box-orient: vertical;">{{item.content}}</text>
				<text style="color: rgb(255, 76, 44);">{{item.mobile}}</text>
			</view>
			
			<view class="" style="padding-left: 20rpx;font-size: 19rpx;color: #999;margin-top: 20rpx;font-weight: 550;">
				{{happenTimeFun(item.createdAt)}}
			</view>
		</view>
		
		
	</view>
</template>


<script>
	export default {
		data(){
			return{
				score_List:[]
			}
		},
		created() {
			this.getScoreData()
		},
		methods:{
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
			getScoreData(){
				uni.request({
					url:this.$elyasApi+'leave/message/findByModal',
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json",
						"accessToken":uni.getStorageSync('token')
					},
					success:(data)=> {
						this.score_List = data.data.data.list;
					}
					
				});
			},
			toArticle(id){
				uni.navigateTo({
					url:'../article-detail/article-detail?id='+id
				})
			}
		}
	}
	
</script>

<style>
	
</style>