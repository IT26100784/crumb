# Order management

Start the existing Spring Boot application with its MySQL database configured, then open `/orders.html` on the application's local address (normally `http://localhost:8080/orders.html`).

The screen uses the existing `/api/orders` endpoints to:

- Create an order with customer ID, required date, agreed total and an optional note.
- List orders and search by order code or customer ID.
- Filter by status.
- Confirm, start baking, mark ready and complete an order.
- Cancel a pending or confirmed order.
- Edit customer ID, required date, agreed total and note on pending or confirmed orders.
- Permanently delete pending or cancelled orders after confirmation.

## CRUD endpoints

| Operation | Endpoint | Result |
|---|---|---|
| Create | `POST /api/orders` | `201` with the created order |
| Read | `GET /api/orders` or `GET /api/orders/{id}` | `200` with orders/order |
| Update | `PUT /api/orders/{id}` | `200` with the updated order |
| Delete | `DELETE /api/orders/{id}` | `204` with no response body |

Create and update accept `customerId`, `requiredDate`, `totalAmount` and optional
`specialNote`. Both require a nonblank customer ID, today or a future required date,
and a positive total. Updates preserve the ID, creation date and status. An overdue
order must be rescheduled to today or later when editing its details.
Missing orders return `404`; edits or deletions forbidden by status return `409`.
Cancellation retains a record; deletion removes it from the database.

The normal status sequence is `PENDING → CONFIRMED → IN_PROGRESS → READY → COMPLETED`. Cancelled orders cannot resume. Cancellation keeps the record rather than deleting it.

New orders require today or a future date. Existing orders may still progress after their required date passes.

This screen is for local development. The account module must enforce staff authorization on the page and API before deployment. The existing API accepts a manually agreed total; customer checkout requires product lookup and server-side pricing before integration. Product items and customer contact details are not stored by this order model.

Run the database-independent lifecycle tests with:

```powershell
.\mvnw.cmd "-Dtest=OrderModelTests,OrderServiceTests" test
```

The existing application context test additionally requires a working database.
