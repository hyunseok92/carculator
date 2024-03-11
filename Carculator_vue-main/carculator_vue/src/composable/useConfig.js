export const config = c => ({
  title: c.title,

  // @ts-expect-error local
  theme: localStorage.getItem('app-theme') ? ref(localStorage.getItem('app-theme')) : c.theme,

  // @ts-expect-error local
  navigationMenu: localStorage.getItem('app-menu') ? ref(localStorage.getItem('app-menu')) : c.navigationMenu,
  isBoxLayout: c.isBoxLayout,

  // @ts-expect-error local
  isVerticalMenuMini: localStorage.getItem('app-menu-mini') ? ref(JSON.parse(localStorage.getItem('app-menu-mini'))) : c.isVerticalMenuMini,
  defaultLocale: c.defaultLocale,
  isRtl: c.isRtl,

  // @ts-expect-error local
  isSemiDark: localStorage.getItem('app-semi-dark') ? ref(JSON.parse(localStorage.getItem('app-semi-dark'))) : c.isSemiDark,

  // @ts-expect-error local
  skins: localStorage.getItem('app-skins') ? ref(localStorage.getItem('app-skins')) : c.skins,

  // @ts-expect-error local
  isNavbarFixed: localStorage.getItem('app-navbar-fixed') ? ref(JSON.parse(localStorage.getItem('app-navbar-fixed'))) : c.isNavbarFixed,
})
