<script setup>
import avatar1 from '@/assets/avatars/avatar-1.png'
import avatar2 from '@/assets/avatars/avatar-2.png'
import avatar3 from '@/assets/avatars/avatar-3.png'
import avatar4 from '@/assets/avatars/avatar-4.png'
import Logo from '@/components/Logo.vue'
import NavUserProfileMenu from '@/components/NavUserProfileMenu.vue'
import { useAppConfig } from '@/composable/useAppConfig'
import { appConfig } from '@appConfig'
import { useTheme } from 'vuetify'

const props = defineProps({
  isDrawerOpen: {
    type: Boolean,
    required: true,
  },
})

const emit = defineEmits(['update:isDrawerOpen'])

const { theme, navigationMenu, isNavbarFixed } = useAppConfig()

const themeVuetify = useTheme()

watch(theme, () => {
  themeVuetify.global.name.value = theme.value
}, { immediate: true })

const themeSwitcherIcon = computed(() => {
  return theme.value === 'dark' ? 'mdi-weather-sunny' : 'mdi-weather-night'
})

const themeSwitcher = () => {
  theme.value = theme.value === 'dark' ? 'light' : 'dark'
}

const emails = [
  {
    avatar: avatar1,
    emailSubject: 'Cupcake pie tart donut donut.',
    date: '30-07-2023',
  },
  {
    avatar: avatar2,
    emailSubject: 'Sesame snaps fruitcake roll pastry.',
    date: '05-06-2023',
  },
  {
    avatar: avatar3,
    emailSubject: 'Jelly beans croissant sugar plum biscuit.',
    date: '10-07-2023',
  },
  {
    avatar: avatar4,
    emailSubject: 'Icing cake dessert bears bonbon.',
    date: '29-07-2023',
  },
]
</script>

<template>
  <VAppBar class="layout-navbar border-b" elevation="0" :absolute="!isNavbarFixed">
    <div class="navbar-wrapper">
      <!-- small screen navigation drawer toggler -->
      <VAppBarNavIcon v-show="$vuetify.display.mdAndDown" class="ms-n3"
        @click="emit('update:isDrawerOpen', !props.isDrawerOpen)" />

      <div v-if="navigationMenu === 'horizontal' && $vuetify.display.lgAndUp"
        class="text-primary d-flex align-center gap-1">
        <Logo />
        <h6 class="text-h6 font-weight-bold text-gradient">
          {{ appConfig.title.value }}
        </h6>
      </div>

      <!-- light/dark theme switcher -->
      <template v-if="navigationMenu === 'vertical'">
        <VBtn :icon="themeSwitcherIcon" variant="text" @click="themeSwitcher" />
      </template>

      <VSpacer />

      <template v-if="navigationMenu === 'horizontal'">
        <VBtn :icon="themeSwitcherIcon" variant="text" @click="themeSwitcher" />
      </template>

      <!-- email -->
      <VBtn icon>
        <VIcon icon="mdi-email-outline" />
        <VMenu activator="parent">
          <VList>
            <VListItem v-for="(item, index) in emails" :key="index" :value="index" :prepend-avatar="item.avatar">
              <VListItemTitle class="font-weight-semibold text-sm">
                {{ item.emailSubject }}
              </VListItemTitle>
              <VListItemSubtitle class="text-xs">
                {{ item.date }}
              </VListItemSubtitle>
            </VListItem>
            <VListItem>
              <VBtn block>
                View All Emails
              </VBtn>
            </VListItem>
          </VList>
        </VMenu>
      </VBtn>

      <!-- Notification -->
      <VBadge dot color="error" location="top" :offset-y="12">
        <VBtn icon class="me-3">
          <VIcon icon="mdi-bell-outline" />
        </VBtn>
      </VBadge>

      <!-- user menu -->
      <NavUserProfileMenu />
    </div>
  </VAppBar>
</template>
