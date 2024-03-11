import { appConfig } from '@appConfig'
import { useTheme } from 'vuetify'

export const useAppConfig = () => {
  const vuetifyTheme = useTheme()

  const navigationMenu = computed({
    get() {
      return appConfig.navigationMenu.value
    },
    set(value) {
      appConfig.navigationMenu.value = value
      localStorage.setItem('app-menu', value)
    },
  })

  const theme = computed({
    get() {
      return appConfig.theme.value
    },
    set(value) {
      appConfig.theme.value = value
      localStorage.setItem('app-theme', value)
      vuetifyTheme.global.name.value = value
    },
  })

  const skins = computed({
    get() {
      return appConfig.skins.value
    },
    set(value) {
      appConfig.skins.value = value
      localStorage.setItem('app-skins', value)
    },
  })

  const isNavbarFixed = computed({
    get() {
      return appConfig.isNavbarFixed.value
    },
    set(value) {
      appConfig.isNavbarFixed.value = value
      localStorage.setItem('app-navbar-fixed', JSON.stringify(value))
    },
  })

  const isBoxLayout = computed({
    get() {
      return appConfig.isBoxLayout.value
    },
    set(value) {
      appConfig.isBoxLayout.value = value
    },
  })

  const isVerticalMenuMini = computed({
    get() {
      return appConfig.isVerticalMenuMini.value
    },
    set(value) {
      appConfig.isVerticalMenuMini.value = value
      localStorage.setItem('app-menu-mini', JSON.stringify(value))
    },
  })

  const isRtl = computed({
    get() {
      return appConfig.isRtl.value
    },
    set(value) {
      appConfig.isRtl.value = value
    },
  })

  const isSemiDark = computed({
    get() {
      return appConfig.isSemiDark.value
    },
    set(value) {
      appConfig.isSemiDark.value = value
      localStorage.setItem('app-semi-dark', JSON.stringify(value))
    },
  })

  const initLoadingTheme = () => {
    watch(theme, () => {
      localStorage.setItem('loader-bg', vuetifyTheme.current.value.colors.surface)
      localStorage.setItem('loader-color', vuetifyTheme.current.value.colors.primary)
    }, {
      immediate: true,
    })
  }

  const rootClasses = () => {
    const classes = ref([])

    watch([appConfig.isBoxLayout, appConfig.navigationMenu, appConfig.skins], () => {
      const layout = appConfig.isBoxLayout.value ? 'v-application-box-layout' : 'v-application-full-layout'
      const menu = appConfig.navigationMenu.value === 'horizontal' ? 'v-theme-horizontal-nav' : 'v-theme-vertical-nav'
      const skin = appConfig.skins.value === 'modern' ? 'v-theme-skins-modern' : 'v-theme-skins-classic'

      classes.value = [layout, menu, skin]
    }, { deep: true, immediate: true })

    return classes
  }

  return { navigationMenu, theme, isBoxLayout, isVerticalMenuMini, isRtl, isSemiDark, skins, isNavbarFixed, initLoadingTheme, rootClasses }
}
