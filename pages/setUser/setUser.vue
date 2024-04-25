<template>
	<view style="padding: 30rpx;">
		
	

				<view  style="height: 200rpx;margin-bottom: 20rpx;margin-left: 80rpx;">
					<u-upload  ref="imgupload" :file-list="fileList"  :action="action" :before-upload="beforeUpload" max-count="1" ></u-upload>
				</view>
				
				
				<view class="formList">
					<view class="formItem">
						<label for="" style="margin-right: 20rpx;">Account</label>
						<input type="text" v-model="userLoginInfo.userName"  placeholder="Account"  />
					</view>
					<!-- 
					<view class="formItem">
						<label for="" style="margin-right: 20rpx;">密码</label>
						<input type="text" v-model="userLoginInfo.password" placeholder="密码" />
					</view> -->
					
					<view class="formItem">
						<label for="" style="margin-right: 20rpx;">E-mail</label>
						<input type="email" v-model="userLoginInfo.email" placeholder="E-mail" />
					</view>
					
					<view class="formItem">
						<label for="" style="margin-right: 20rpx;">Phone</label>
						<input type="phone" v-model="userLoginInfo.phone" placeholder="Phone" />
					</view>
					
					
				</view>
				
				<view class="loginBtn" @click="toLogin">
					Change
				</view>
		
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				action:this.$elyasApi+'file/uploadSingle',
				userDetailInfo:[],
				setData:[],
				fileList: [],
				fileList1:[{
						url: '',
					}],
				userLoginInfo:{
					id:'',
					userName:'',
					password:123456,
					email:'',
					avatar:'',
					phone:'',
				
				},
			}
		},
		onLoad(option) {
			this.getUserInfoByToken()
			
			if (option.avatar) {
				this.fileList.push({
					url: option.avatar
				})
			}
			
			if (option.userName) {
				this.userLoginInfo.userName = option.userName
			}
			if (option.id) {
				this.userLoginInfo.id = option.id
			}
			
			
		},
		methods: {
			getUserInfoByToken(){
				uni.request({
					url:this.$elyasApi+'user/detailByToken',
					method:'POST',
					data:{},
					header:{
						"content-type":"application/json",
						"accessToken":uni.getStorageSync('token')
					},
					success:(data)=> {
						this.userDetailInfo = data.data.data;
						this.userLoginInfo.userName = data.data.data.userName
						this.userLoginInfo.email = data.data.data.email
						this.userLoginInfo.password = data.data.data.password
						this.userLoginInfo.phone = data.data.data.phone
						this.userLoginInfo.avatar = data.data.data.avatar
						this.userLoginInfo.id = data.data.data.id
						
						if (this.userLoginInfo.avatar != '') {
							this.fileList.push({
								url: this.userLoginInfo.avatar
							})
						}
						
						
						
					}
					
				});
			},
			toLogin(){
							uni.request({
								url:this.$elyasApi+'user/update',
								method:'POST',
								data:this.userLoginInfo,
								header:{
								    "content-type":"application/json",
									"accessToken":uni.getStorageSync('token')
								},
								success:(data)=> {
									
									
								uni.showToast({
									title:'Changed successfully',
								});
									
									
									
						
									
									
								}
								
							})
						},
			async beforeUpload(index,list){
				let that = this
				// var list = list
				
				//  // await var imgUrl = list[0].response.data
				// console.log(list)
				setTimeout(()=>{
					// 要赋值给谁
					that.userLoginInfo.avatar = list[0].response.data
					console.log(that.userLoginInfo.avatar);
				},3000)
				// for(var i = 0; i < list.length; i++) {
				// 	console.log(list[0].response,'1321')
					
				// }
			},
			deletePic(event) {
				this[`fileList${event.name}`].splice(event.index, 1)
			},
			toRegister(){
				uni.navigateTo({
					url:'../login/login'
				})
			}
		}
	}
</script>

<style>
.formItem{
		height: 100rpx;
		padding-top: 56rpx;
		border-bottom: 1rpx solid rgb(33, 118, 255);
		margin: 30rpx 100rpx;
		display: flex;
		align-items: center;
	}
	
	.formItem input{
		font-size: 28rpx;
	}
	
	.loginBtn {
		height: 86rpx;
		margin: 0 100rpx;
		color: #fff;
		background-color: rgb(33, 118, 255);
		text-align: center;
		line-height: 86rpx;
		font-size: 30rpx;
		margin-top: 60rpx;
	}

</style>
