Frontend:
/ -> vista principal
/login -> login
/register -> registro
/dashboard -> panel principal
/accounts -> listado de cuentas
/accounts/ID -> detalle de una cuenta, reemplaza ID por el id real
/loans -> préstamos
/loans/ID -> detalle de un préstamo, reemplaza ID por el id real
/transfers -> transferencias
/transactions -> transacciones
/loan-applications -> solicitudes de préstamo
/profile -> perfil del usuario
Ejemplos directos:

http://localhost:3000/login
http://localhost:3000/register
http://localhost:3000/dashboard
http://localhost:3000/accounts
http://localhost:3000/accounts/550e8400-e29b-41d4-a716-446655440000
http://localhost:3000/loans
http://localhost:3000/loans/550e8400-e29b-41d4-a716-446655440000

Backend:
Base URL:
http://localhost:8081

Auth

POST /api/auth/register
Requiere body JSON:
{
"dni": "12345678",
"firstName": "Juan",
"lastName": "Perez",
"email": "juan.perez@test.com",
"password": "123456",
"phone": "999888777",
"role": "CUSTOMER"
}

POST /api/auth/login
Requiere body JSON:
{
"email": "juan.perez@test.com",
"password": "123456"
}

Accounts

GET /api/accounts
No requiere body.

GET /api/accounts/{id}
No requiere body.
Ejemplo:
GET /api/accounts/550e8400-e29b-41d4-a716-446655440000

POST /api/accounts
Requiere body JSON:
{
"accountNumber": "001-000000001",
"currency": "PEN",
"userId": "550e8400-e29b-41d4-a716-446655440000"
}

Profiles

GET /api/profiles
No requiere body.

GET /api/profiles/{id}
No requiere body.

POST /api/profiles
Requiere body JSON:
{
"dni": "87654321",
"firstName": "Maria",
"lastName": "Lopez",
"email": "maria.lopez@test.com",
"password": "123456",
"phone": "988777666",
"role": "CUSTOMER"
}

Loans

GET /api/loans
No requiere body.

GET /api/loans/{id}
No requiere body.

POST /api/loans
Requiere body JSON:
{
"requestedAmount": 10000.00,
"termMonths": 24,
"purpose": "Capital de trabajo",
"monthlyIncome": 3500.00,
"userId": "550e8400-e29b-41d4-a716-446655440000"
}

Loan Applications

GET /api/loan-applications
No requiere body.

POST /api/loan-applications
Requiere body JSON:
{
"requestedAmount": 15000.00,
"termMonths": 36,
"purpose": "Compra de equipos",
"monthlyIncome": 4200.00,
"userId": "550e8400-e29b-41d4-a716-446655440000"
}

Transactions

GET /api/transactions
No requiere body.

GET /api/transactions/account/{accountId}
No requiere body.
Ejemplo:
GET /api/transactions/account/550e8400-e29b-41d4-a716-446655440000

Transfers

POST /api/transfers
Requiere body JSON:
{
"senderAccountId": "11111111-1111-1111-1111-111111111111",
"receiverAccountId": "22222222-2222-2222-2222-222222222222",
"amount": 150.50,
"description": "Transferencia de prueba"
}