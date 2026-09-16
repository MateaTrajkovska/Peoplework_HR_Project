<script setup lang="ts">
import { fullName, initials } from '../store';
import type { Employee } from '../types';

defineProps<{ employee?: Employee; subtitle?: string }>();
</script>

<template>
  <div class="person">
    <span class="avatar" :class="employee?.avatarColor || 'purple'">{{ initials(employee) }}</span>
    <div>
      <strong>{{ fullName(employee) }}</strong>
      <small>{{ subtitle ?? employee?.email }}</small>
    </div>
  </div>
</template>