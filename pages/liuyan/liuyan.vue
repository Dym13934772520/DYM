<template>
	<view style="padding: 30rpx;">
		
		<view>
			<input type="text" style="border: 1px solid #999;border-radius: 5px;padding: 20rpx;" placeholder="Please inter your name" v-model="name">
		</view>
		
		<view>
			<input type="text" style="border: 1px solid #999;border-radius: 5px;padding: 20rpx;margin-top: 15rpx;" placeholder="Please inter your phone" v-model="mobile">
		</view>
		
		<view style="margin-top: 15rpx;border: 1px solid #999;border-radius: 5px;padding: 20rpx;">
			<textarea name="" id="" cols="30" rows="10" v-model="contant" placeholder="Please inter your message"></textarea>
			
		</view>
		
		<view class="" style="margin-top: 100rpx;padding: 0 30rpx;">
			<button style="background-color: rgb(33, 118, 255);color: #fff;border-radius: 30rpx;" @click="sendMessaga()">Submit</button>
		</view>	
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				contant:'',
				mobile:'',
				name:''
			}
		},
		methods: {
			sendMessaga(){
				uni.request({
					url:this.$elyasApi+'leave/message/add',
					method:'POST',
					data:{content:this.contant,name:this.name,mobile:this.mobile},
					header:{
						"content-type":"application/json",
						"accessToken":uni.getStorageSync('token')
					},
					success:(data)=> {
						if (data.data.code !== 200) {
							uni.showToast({
								title:data.data.message
							});
							return;
						}else {
							uni.showToast({
								title:'Release success'
							})
							
							setTimeout(()=>{
								this.contant = ''
								this.mobile = ''
								this.name = ''
							},400)
						}
					}
					
				});
				
				
			}
		}
	}
</script>

<style>

</style>
