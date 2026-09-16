# Peoplework frontend
Vue 3 + TypeScript + Vue Router + Vite. The source was rebuilt around the employee, contract and absence modules in the supplied HR portal. The original option catalogs remain in `src/constants`.

```sh
npm ci
npm run dev
```
Open http://localhost:5173. API calls use the Vite proxy to http://localhost:8648; the backend must be running. For a different backend use `API_PROXY_TARGET`. Run `npm run build` for a type-checked production build.

Fonts and icons are local npm assets; no remote font service is needed at runtime.
See the root README for authentication, database setup, complete application startup and rebuilding the executable JAR.
