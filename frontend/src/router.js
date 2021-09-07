
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import StockManager from "./components/StockManager"

import ReservationManager from "./components/ReservationManager"

import PaymentManager from "./components/PaymentManager"

import DeliveryManager from "./components/DeliveryManager"


import MyPage from "./components/MyPage"
export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/stocks',
                name: 'StockManager',
                component: StockManager
            },

            {
                path: '/reservations',
                name: 'ReservationManager',
                component: ReservationManager
            },

            {
                path: '/payments',
                name: 'PaymentManager',
                component: PaymentManager
            },

            {
                path: '/deliveries',
                name: 'DeliveryManager',
                component: DeliveryManager
            },


            {
                path: '/myPages',
                name: 'MyPage',
                component: MyPage
            },


    ]
})
