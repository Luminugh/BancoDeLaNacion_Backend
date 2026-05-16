-- Base schema for the bank backend.
-- This script is intentionally simple and aligns with the initial Spring Boot entities.

create extension if not exists "pgcrypto";

create type user_role as enum (
    'CUSTOMER',
    'EVALUATOR',
    'COMMITTEE',
    'ADMIN'
);

create type account_status as enum (
    'ACTIVE',
    'INACTIVE',
    'BLOCKED'
);

create type transaction_type as enum (
    'DEPOSIT',
    'WITHDRAWAL',
    'TRANSFER_SENT',
    'TRANSFER_RECEIVED',
    'LOAN_PAYMENT'
);

create type transfer_status as enum (
    'PENDING',
    'COMPLETED',
    'FAILED'
);

create type loan_status as enum (
    'ACTIVE',
    'COMPLETED',
    'LATE'
);

create type installment_status as enum (
    'PENDING',
    'PAID',
    'OVERDUE'
);

create type application_status as enum (
    'SUBMITTED',
    'EVALUATION',
    'APPROVED',
    'REJECTED',
    'DISBURSED'
);

create table if not exists public.profiles (
    id uuid primary key default gen_random_uuid(),
    dni varchar(8) not null unique,
    first_name varchar(100) not null,
    last_name varchar(100) not null,
    email varchar(150) not null unique,
    password varchar(255) not null,
    phone varchar(20),
    role user_role not null default 'CUSTOMER',
    created_at timestamptz not null default now()
);

create table if not exists public.accounts (
    id uuid primary key default gen_random_uuid(),
    account_number varchar(20) not null unique,
    balance numeric(14,2) not null default 0,
    currency varchar(10) not null default 'PEN',
    status account_status not null default 'ACTIVE',
    user_id uuid not null references public.profiles(id),
    created_at timestamptz not null default now()
);

create table if not exists public.transactions (
    id uuid primary key default gen_random_uuid(),
    transaction_type transaction_type not null,
    amount numeric(14,2) not null,
    description text,
    account_id uuid not null references public.accounts(id),
    created_at timestamptz not null default now()
);

create table if not exists public.transfers (
    id uuid primary key default gen_random_uuid(),
    amount numeric(14,2) not null,
    status transfer_status not null default 'PENDING',
    sender_account_id uuid not null references public.accounts(id),
    receiver_account_id uuid not null references public.accounts(id),
    created_at timestamptz not null default now()
);

create table if not exists public.loan_applications (
    id uuid primary key default gen_random_uuid(),
    requested_amount numeric(14,2) not null,
    term_months integer not null,
    purpose text,
    monthly_income numeric(14,2),
    status application_status not null default 'SUBMITTED',
    user_id uuid not null references public.profiles(id),
    created_at timestamptz not null default now()
);

create table if not exists public.loans (
    id uuid primary key default gen_random_uuid(),
    loan_number varchar(30) not null unique,
    amount numeric(14,2) not null,
    interest_rate numeric(5,2) not null,
    term_months integer not null,
    monthly_payment numeric(14,2),
    remaining_balance numeric(14,2),
    status loan_status not null default 'ACTIVE',
    disbursement_date date,
    user_id uuid not null references public.profiles(id),
    created_at timestamptz not null default now()
);

create table if not exists public.loan_installments (
    id uuid primary key default gen_random_uuid(),
    installment_number integer not null,
    due_date date not null,
    amount numeric(14,2) not null,
    paid_amount numeric(14,2) not null default 0,
    payment_date timestamptz,
    status installment_status not null default 'PENDING',
    loan_id uuid not null references public.loans(id),
    created_at timestamptz not null default now(),
    unique (loan_id, installment_number)
);

create table if not exists public.payments (
    id uuid primary key default gen_random_uuid(),
    amount numeric(14,2) not null,
    payment_method varchar(30),
    loan_id uuid not null references public.loans(id),
    created_at timestamptz not null default now()
);

create table if not exists public.audit_logs (
    id uuid primary key default gen_random_uuid(),
    action varchar(100) not null,
    table_name varchar(100) not null,
    record_id uuid,
    user_id uuid references public.profiles(id),
    created_at timestamptz not null default now()
);

create index if not exists idx_accounts_user_id on public.accounts(user_id);
create index if not exists idx_transactions_account_id on public.transactions(account_id);
create index if not exists idx_transfers_sender_account_id on public.transfers(sender_account_id);
create index if not exists idx_transfers_receiver_account_id on public.transfers(receiver_account_id);
create index if not exists idx_loans_user_id on public.loans(user_id);
create index if not exists idx_loan_applications_user_id on public.loan_applications(user_id);
create index if not exists idx_loan_installments_loan_id on public.loan_installments(loan_id);
create index if not exists idx_payments_loan_id on public.payments(loan_id);

-- End of plain PostgreSQL bank schema.