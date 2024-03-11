import { config } from '@/composable/useConfig'

// default settings
export const appConfig = config({
  title: ref('Carculator'),
  theme: ref('light'),
  navigationMenu: ref('horizontal'),
  isBoxLayout: ref(true),
  isVerticalMenuMini: ref(false),
  defaultLocale: ref('en'),
  isRtl: ref(false),
  isSemiDark: ref(false),
  skins: ref('modern'),
  isNavbarFixed: ref(true),
})
