<?php

namespace App\Imports;

use App\Models\User;
use Illuminate\Support\Facades\Hash;
use Maatwebsite\Excel\Concerns\ToModel;
use Maatwebsite\Excel\Concerns\WithHeadingRow;
use Maatwebsite\Excel\Concerns\WithValidation;

class UsersImport implements ToModel, WithHeadingRow, WithValidation
{
    public function model(array $row)
    {
        return new User([
            'name' => $row['name'] ?? $row['nama'],
            'email' => $row['email'],
            'password' => Hash::make($row['password'] ?? 'password123'),
            'role' => $row['role'] ?? 'siswa',
        ]);
    }

    public function rules(): array
    {
        return [
            'email' => 'required|email|unique:users,email',
            '*.email' => 'required|email|unique:users,email',
        ];
    }
}
