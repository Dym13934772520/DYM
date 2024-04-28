import Vue from 'vue'
import Vuex from 'vuex'
Vue.use(Vuex);

import $C from '@/common/config.js';

export default new Vuex.Store({
	state:{
		loginStatus:false,
		userInfo:{},
		isOpen:false,
		// socket链接状态
		isSoket:false,
		// soket task  
		SocketTask:false,
		// 是否上线
		isOnline:false,
		
		msgList: [],
		
		
		
	},
	mutations:{
		// 登录方法
		login(state,userInfo){
			state.loginStatus = true;
			state.userInfo = userInfo;
			uni.setStorageSync('userInfo',JSON.stringify(userInfo));
		},
		addMsg(state, msg) {
			state.msgList.push(msg)
		}
	},
	actions:{
		// 链接soket
		openSoket({ state,dispatch }){
			// 防止重复链接
			if(state.isOpen) return;
			
			state.SocketTask = uni.connectSocket({   
			  url: 'ws://localhost:7007/socket/' + state.userInfo.userName,
			  complete:()=>{}
			});
			
			if (!state.SocketTask) return;
			
			state.SocketTask.onOpen(()=>{
				console.log('The connection status is set to Connected');
				state.isOpen = true
			})
			
			state.SocketTask.onClose(()=>{
				console.log('Connection closed');
				state.isOpen = false
				state.SocketTask = false
				
			})
			
			state.SocketTask.onError(()=>{
				console.log('Connection error');
				state.isOpen = false
				state.SocketTask = false
			})
			
			state.SocketTask.onMessage((e)=>{
				state.msgList.push({
					from: 'other',
					msg: e.data
				})
			})
			
			
			
		}
	}
})