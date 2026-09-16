<script setup lang="ts">
import { nextTick, onBeforeUnmount, watch, ref } from 'vue';
import { modal } from '../forms';
import { errorMessage } from '../store';
import Icon from './Icon.vue';

const dialog = ref<HTMLDialogElement>();
let previous: HTMLElement | null = null;

watch(
    () => modal.open,
    async (open) => {
      if (open) {
        previous = document.activeElement as HTMLElement;
        await nextTick();
        dialog.value?.showModal();
        dialog.value?.querySelector<HTMLInputElement>('input,select,textarea')?.focus();
        document.body.style.overflow = 'hidden';
      } else {
        dialog.value?.close();
        document.body.style.overflow = '';
        previous?.focus();
      }
    }
);

onBeforeUnmount(() => (document.body.style.overflow = ''));

function close() {
  if (!modal.busy) modal.open = false;
}

async function submit() {
  modal.busy = true;
  modal.error = '';
  try {
    await modal.submit?.();
    modal.open = false;
  } catch (e) {
    modal.error = errorMessage(e);
  } finally {
    modal.busy = false;
  }
}
</script>

<template>
  <dialog ref="dialog" class="form-dialog" aria-labelledby="dialog-title" @cancel.prevent="close">
    <form @submit.prevent="submit">
      <header>
        <div>
          <span class="eyebrow">PEOPLEWORK / WORKSPACE</span>
          <h2 id="dialog-title">{{ modal.title }}</h2>
          <p>{{ modal.description }}</p>
        </div>
        <button type="button" class="icon-btn" :disabled="modal.busy" aria-label="Close dialog" @click="close">
          <Icon name="close" />
        </button>
      </header>

      <section class="form-grid">
        <label v-for="f in modal.fields" :key="f.key" :class="{ wide: f.wide }">
          {{ f.label }}
          <span v-if="f.required" class="required"> *</span>

          <select
              v-if="f.type==='select'"
              v-model="modal.values[f.key]"
              :required="f.required"
              :disabled="modal.busy || f.readonly"
          >
            <option value="" disabled>Select {{ f.label.toLowerCase() }}</option>
            <option v-for="o in f.options" :key="o.value" :value="o.value">{{ o.label }}</option>
          </select>

          <textarea
              v-else-if="f.type==='textarea'"
              v-model="modal.values[f.key]"
              :required="f.required"
              :disabled="modal.busy"
              :rows="f.key==='body' ? 12 : 3"
              :maxlength="f.key==='body' ? 12000 : 1000"
          ></textarea>

          <input
              v-else
              v-model="modal.values[f.key]"
              :type="f.type || 'text'"
              :required="f.required"
              :disabled="modal.busy || f.readonly"
              :min="f.min"
              :max="f.max"
              :step="f.type==='number' ? (f.key==='salary' ? '0.01' : '1') : undefined"
              :maxlength="f.key==='bankAccount' ? 18 : 255"
          />

          <small v-if="f.help">{{ f.help }}</small>
        </label>
      </section>

      <div v-if="modal.error" class="alert" role="alert">{{ modal.error }}</div>

      <footer>
        <span v-if="modal.fields.some(f=>f.required)" class="muted">* Required fields</span>
        <div>
          <button type="button" class="btn" :disabled="modal.busy" @click="close">Cancel</button>
          <button class="btn" :class="modal.danger ? 'danger' : 'primary'" :disabled="modal.busy">
            {{ modal.busy ? 'Saving…' : modal.submitText }}
            <Icon v-if="!modal.danger" name="arrow-right" />
          </button>
        </div>
      </footer>
    </form>
  </dialog>
</template>