<script setup>
import SuspenseLoader from './components/Loader.vue'

const isFallbackStateActive = ref(false)
const refLoadingIndicator = ref(null)

watch([
  isFallbackStateActive,
  refLoadingIndicator,
], () => {
  if (isFallbackStateActive.value && refLoadingIndicator.value)
    refLoadingIndicator.value.fallbackHandle()
  if (!isFallbackStateActive.value && refLoadingIndicator.value)
    refLoadingIndicator.value.resolveHandle()
}, { immediate: true })
</script>

<template>
  <RouterView v-slot="{ Component }">
    <Suspense
      :timeout="0"
      @fallback="isFallbackStateActive = true"
      @resolve="isFallbackStateActive = false"
    >
      <Component :is="Component" />
    </Suspense>
  </RouterView>

  <SuspenseLoader ref="refLoadingIndicator" />
</template>
