<?php

use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| Web Routes - WarungGo Admin Dashboard
|--------------------------------------------------------------------------
*/

// Redirect root to login or dashboard
Route::get('/', function () {
    return redirect()->route('login');
});

// Auth Routes
Route::get('/login', function () {
    return view('pages.auth.login');
})->name('login');

// Dashboard
Route::get('/dashboard', function () {
    return view('pages.dashboard');
})->name('dashboard');

// Products & Stock Management
Route::get('/products', function () {
    return view('pages.products');
})->name('products');

// Transactions
Route::get('/transactions', function () {
    return view('pages.transactions');
})->name('transactions');

// Reports
Route::get('/reports', function () {
    return view('pages.reports');
})->name('reports');

// User Management
Route::get('/users', function () {
    return view('pages.users');
})->name('users');

// Profile
Route::get('/profile', function () {
    return view('pages.profile');
})->name('profile');

// Settings
Route::get('/settings', function () {
    return view('pages.settings');
})->name('settings');
