<script setup>
import { register } from 'swiper/element/bundle'
import swiper1 from '@/assets/swiper/car1.jpg'
import swiper2 from '@/assets/swiper/car2.jpg'
import swiper3 from '@/assets/swiper/car3.jpg'

register()
</script>
<template>

<VRow>
  <VCol cols="12">
  <VCard>

   <swiper-container
    class="mySwiper"
    pagination="true"
    navigation="true"
    space-between="30"
    centered-slides="true"
    autoplay-delay="2500"
    autoplay-disable-on-interaction="false"
    events-prefix="swiper-"
  >
    <swiper-slide
      v-for="swiperImg in [
        swiper3
       
      ]"
      :key="swiperImg"
    >
      <VImg
        :src="swiperImg"
        height="500"
        
        cover
        gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
      />
    </swiper-slide>
  </swiper-container>
 
  </VCard>
  </VCol>
    <VCol cols="6">
  <VCard>
   <swiper-container
    class="mySwiper"
    pagination="true"
    navigation="true"
    space-between="30"
    centered-slides="true"
    autoplay-delay="2500"
    autoplay-disable-on-interaction="false"
    events-prefix="swiper-"
  >
    <swiper-slide
      v-for="swiperImg in [
      
        swiper2,
       
      ]"
      :key="swiperImg"
    >
      <VImg
        :src="swiperImg"
        height="500"
        
        cover
        gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
      />
    </swiper-slide>
  </swiper-container>
 
  </VCard>
  </VCol>
    <VCol cols="6">
  <VCard>
   <swiper-container
    class="mySwiper"
    pagination="true"
    navigation="true"
    space-between="30"
    centered-slides="true"
    autoplay-delay="2500"
    autoplay-disable-on-interaction="false"
    events-prefix="swiper-"
  >
    <swiper-slide
      v-for="swiperImg in [
        
        swiper1,
      ]"
      :key="swiperImg"
    >
      <VImg
        :src="swiperImg"
        height="500"
        
        cover
        gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
      />
    </swiper-slide>
  </swiper-container>
 
  </VCard>
  </VCol>
</VRow>
</template>
