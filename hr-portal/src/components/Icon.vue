<script setup lang="ts">
defineProps<{ name: string }>();
</script>

<template>
  <i :class="'mdi mdi-'+name" aria-hidden="true"></i>
</template>