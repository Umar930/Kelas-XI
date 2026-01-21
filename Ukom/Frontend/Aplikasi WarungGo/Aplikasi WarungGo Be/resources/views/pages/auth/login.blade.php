<x-layouts.auth title="Login">
    <div class="sm:mx-auto sm:w-full sm:max-w-md">
        <!-- Logo -->
        <div class="flex justify-center">
            <div class="flex items-center gap-3">
                <div class="flex items-center justify-center w-14 h-14 bg-emerald-600 rounded-2xl shadow-lg">
                    <svg class="w-8 h-8 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z"></path>
                    </svg>
                </div>
            </div>
        </div>
        <h2 class="mt-6 text-center text-3xl font-bold tracking-tight text-gray-900">
            WarungGo
        </h2>
        <p class="mt-2 text-center text-sm text-gray-600">
            Masuk ke Admin Panel
        </p>
    </div>

    <div class="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div class="bg-white py-8 px-6 shadow-xl rounded-2xl sm:px-10 border border-gray-200">
            <form 
                class="space-y-6" 
                x-data="{ 
                    loading: false, 
                    showPassword: false,
                    email: '',
                    password: '',
                    remember: false,
                    errors: {}
                }"
                @submit.prevent="loading = true; setTimeout(() => { loading = false; window.location.href = '{{ route('dashboard') }}' }, 1500)"
            >
                <!-- Email -->
                <div>
                    <label for="email" class="block text-sm font-medium text-gray-700 mb-1.5">
                        Email
                    </label>
                    <div class="relative">
                        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                            <svg class="h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 12a4 4 0 10-8 0 4 4 0 008 0zm0 0v1.5a2.5 2.5 0 005 0V12a9 9 0 10-9 9m4.5-1.206a8.959 8.959 0 01-4.5 1.207"></path>
                            </svg>
                        </div>
                        <input 
                            type="email" 
                            id="email"
                            name="email"
                            x-model="email"
                            placeholder="admin@warunggo.com"
                            required
                            autocomplete="email"
                            class="block w-full pl-10 pr-4 py-2.5 text-gray-900 border border-gray-300 rounded-lg bg-white focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-colors duration-200"
                            :class="errors.email ? 'border-red-500 focus:ring-red-500 focus:border-red-500' : ''"
                        >
                    </div>
                    <template x-if="errors.email">
                        <p class="mt-1.5 text-sm text-red-600" x-text="errors.email"></p>
                    </template>
                </div>

                <!-- Password -->
                <div>
                    <label for="password" class="block text-sm font-medium text-gray-700 mb-1.5">
                        Password
                    </label>
                    <div class="relative">
                        <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                            <svg class="h-5 w-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z"></path>
                            </svg>
                        </div>
                        <input 
                            :type="showPassword ? 'text' : 'password'"
                            id="password"
                            name="password"
                            x-model="password"
                            placeholder="••••••••"
                            required
                            autocomplete="current-password"
                            class="block w-full pl-10 pr-12 py-2.5 text-gray-900 border border-gray-300 rounded-lg bg-white focus:ring-2 focus:ring-emerald-500 focus:border-emerald-500 transition-colors duration-200"
                            :class="errors.password ? 'border-red-500 focus:ring-red-500 focus:border-red-500' : ''"
                        >
                        <button 
                            type="button"
                            @click="showPassword = !showPassword"
                            class="absolute inset-y-0 right-0 pr-3 flex items-center text-gray-400 hover:text-gray-600"
                        >
                            <svg x-show="!showPassword" class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"></path>
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M2.458 12C3.732 7.943 7.523 5 12 5c4.478 0 8.268 2.943 9.542 7-1.274 4.057-5.064 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"></path>
                            </svg>
                            <svg x-show="showPassword" class="h-5 w-5" fill="none" stroke="currentColor" viewBox="0 0 24 24" style="display: none;">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.875 18.825A10.05 10.05 0 0112 19c-4.478 0-8.268-2.943-9.543-7a9.97 9.97 0 011.563-3.029m5.858.908a3 3 0 114.243 4.243M9.878 9.878l4.242 4.242M9.88 9.88l-3.29-3.29m7.532 7.532l3.29 3.29M3 3l3.59 3.59m0 0A9.953 9.953 0 0112 5c4.478 0 8.268 2.943 9.543 7a10.025 10.025 0 01-4.132 5.411m0 0L21 21"></path>
                            </svg>
                        </button>
                    </div>
                    <template x-if="errors.password">
                        <p class="mt-1.5 text-sm text-red-600" x-text="errors.password"></p>
                    </template>
                </div>

                <!-- Remember Me & Forgot Password -->
                <div class="flex items-center justify-between">
                    <div class="flex items-center">
                        <input 
                            id="remember"
                            name="remember"
                            type="checkbox"
                            x-model="remember"
                            class="h-4 w-4 text-emerald-600 border-gray-300 rounded focus:ring-emerald-500 cursor-pointer"
                        >
                        <label for="remember" class="ml-2 block text-sm text-gray-700 cursor-pointer">
                            Ingat saya
                        </label>
                    </div>

                    <div class="text-sm">
                        <a href="#" class="font-medium text-emerald-600 hover:text-emerald-500 transition-colors">
                            Lupa password?
                        </a>
                    </div>
                </div>

                <!-- Submit Button -->
                <div>
                    <button 
                        type="submit"
                        :disabled="loading"
                        class="relative w-full flex justify-center items-center py-3 px-4 border border-transparent rounded-lg shadow-sm text-sm font-medium text-white bg-emerald-600 hover:bg-emerald-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500 transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                        <svg 
                            x-show="loading"
                            class="animate-spin -ml-1 mr-2 h-5 w-5 text-white" 
                            xmlns="http://www.w3.org/2000/svg" 
                            fill="none" 
                            viewBox="0 0 24 24"
                            style="display: none;"
                        >
                            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                        </svg>
                        <span x-text="loading ? 'Memproses...' : 'Masuk'">Masuk</span>
                    </button>
                </div>
            </form>

            <!-- Demo Credentials -->
            <div class="mt-6">
                <div class="relative">
                    <div class="absolute inset-0 flex items-center">
                        <div class="w-full border-t border-gray-200"></div>
                    </div>
                    <div class="relative flex justify-center text-sm">
                        <span class="px-2 bg-white text-gray-500">Demo Credentials</span>
                    </div>
                </div>
                <div class="mt-4 p-4 bg-gray-50 rounded-lg border border-gray-200">
                    <div class="flex items-center justify-between text-sm">
                        <div>
                            <p class="text-gray-600">Email: <span class="font-mono text-gray-900">admin@warunggo.com</span></p>
                            <p class="text-gray-600">Password: <span class="font-mono text-gray-900">password123</span></p>
                        </div>
                        <button 
                            type="button"
                            @click="email = 'admin@warunggo.com'; password = 'password123'"
                            class="text-emerald-600 hover:text-emerald-700 font-medium"
                        >
                            Gunakan
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <p class="mt-6 text-center text-sm text-gray-500">
            &copy; 2024 WarungGo. All rights reserved.
        </p>
    </div>
</x-layouts.auth>
