<template>
	<view class="login-wrapper">
		<img class="top-img" src="/static/login-top-pic.png" alt="">
		<view class="white-box">
			<text class="login-title" >Login</text>
			<text class="login-p">Wellcome 😀</text>
			<view class="exit-btn">
				<input type="text" placeholder="Account" style="border: 1px solid #666;margin-bottom: 10px;padding: 13rpx 0;padding-left: 1em;border-radius: 10rpx;" v-model="userSend.userName">
				<input type="password" placeholder="Password" style="border: 1px solid #666;padding: 13rpx 0;padding-left: 1em;border-radius: 10rpx;" v-model="userSend.password">
			</view>
			<button class="login-btn" @click="toLogin">Login</button>
			<button class="exit-btn2" @click="toAccountLogin">Register</button>
			
			
			
		
			<view class="select-wrap">
				<label style="font-weight: 550;">
					<checkbox value="" :checked="checkedStatus" class="Ck" @click="changeCheckedStatus" /><text>By logging in, you agree<text style="color: rgb(56, 120, 255);">《Service Negotiation》</text>and<text style="color: rgb(56, 120, 255);">《Privacy Policy》</text>and<text style="color: rgb(56, 120, 255);">《Merchant Privacy Protection Guidelines》</text></text>
				</label>
			</view>
		</view>
		
	</view>
</template>

<script>
	let that = null;
	export default {
		data() {
			return {
				checkedStatus:false,
				sessionKey:'',
				signature:'',
				rawData:'',
				encryptedData:'',
				iv:'',
				userSend:{
					userName:'user',
					password:'123456'
				}
				
			}
		},
		onLoad() {
			that=this;
		},
		methods: {
			// toPage1(){
			// 	uni.navigateTo({
			// 		url:'../select-target/select-target'
			// 	})
			// },
			// 登录按钮
			changeCheckedStatus(){
				this.checkedStatus =! this.checkedStatus
				
			},
			toLogin(){
				if (that.checkedStatus === false) {
					console.log('Not agree');
					uni.showToast({
						title:'Please click agree to proceed',
					});
					return;
					
				}else {
					uni.showToast({
						title:'Agree！',
					});
					uni.request({
						url:that.$elyasApi+'user/login',
						method:'POST',
						data:{
							userName:this.userSend.userName,
							password:this.userSend.password
						},
						header:{
							"content-type":"application/json"
						},
						success:(data)=> {
							
							if (data.data.code !== 200) {
								uni.showToast({
									title:data.data.message,
								});
								
								return;
								
							}else {
								that.$store.commit('login',data.data.data);
								uni.setStorageSync('token',data.data.data.token)
								console.log(data.data.data,'data');
								
								uni.showToast({
									title:'Login success',
								});
								
								
								
								
								setTimeout(function(){
									uni.switchTab({
										url:'../index/index'
									})
								},400)
							}
							
							
						}
						
					});
				}
				
				
				
				
		
			},
			// 取消
			back(){
				uni.navigateBack({
					delta:1
				})
			},
			toAccountLogin(){
				uni.navigateTo({
					url:'/pages/register/register'
				})
			}
		}
	}
</script>

<style>
	.login-wrapper {
		background-color: rgb(33, 118, 255);
		width: 375px;
		height: 812px;
	}
	
	
	.top-img {
		position: absolute;
		width: 324px;
		height: 218px;
		top: 94px;
		left: 25px;
	}
	
	.white-box{
		position: absolute;
		width: 375px;
		height: 500px;
		top: 310px;
		left: -2px;
		background: rgb(255, 255, 255);
		border-radius: 30px 30px 0px 0px;
	}
	
	.login-title {
		position: absolute;
		width: 40px;
		height: 20px;
		top: 21px;
		left: 163px;
		color: rgb(53, 56, 58);
		font-family: Noto Sans SC;
		font-size: 20px;
		font-weight: 900;
		line-height: 20px;
		letter-spacing: 0px;
		text-align: center;
		text-transform: uppercase;
	}
	
	.login-p {
		position: absolute;
		width: 280px;
		height: 27px;
		top: 75px;
		left: 0px;
		color: rgb(76, 75, 75);
		font-family: Noto Sans SC;
		font-size: 16px;
		font-weight: 500;
		line-height: 27px;
		letter-spacing: 0px;
		text-align: center;
	}
	
	.login-btn{
		position: absolute;
		width: 326px;
		height: 45px;
		top: 250px;
		left: 25px;
		color: #fff;
		background: rgb(33, 118, 255);
		border-radius: 22.5px;
	}
	
	.exit-btn{
		position: absolute;
		width: 327px;
		height: 42px;
		top: 150px;
		left: 25px;
		font-weight: 550;
		background: rgb(241, 246, 255);
		border-radius: 21px;
	}
	
	.exit-btn2{
		position: absolute;
		width: 327px;
		height: 42px;
		top: 310px;
		left: 25px;
		font-weight: 550;
		background: rgb(241, 246, 255);
		border-radius: 21px;
	}
	
	.get-phone-btn{
		position: absolute;
		width: 326px;
		height: 45px;
		top: 324px;
		left: 25px;
		color: #fff;
		background: rgb(33, 118, 255);
		border-radius: 22.5px;
	}
	
	.select-wrap{
		position: absolute;
		width: 284px;
		height: 24px;
		top: 410px;
		left: 50px;
		font-family: Noto Sans SC;
		font-size: 13px;
		font-weight: 500;
		line-height: 19px;
		letter-spacing: 0px;
		text-align: left;
	}
	
	.Ck{
		transform: scale(0.7);
	}
	
	
</style>
