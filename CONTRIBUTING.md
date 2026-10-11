# Contributing to Crumb Bakery

Thanks for contributing. This is a six-member student project, so keep each change focused on the feature assigned to you and coordinate shared files before editing them.

## 1. Branches and ownership

Use the feature branches agreed by the group:

- `order` — Order
- `deliveryAndPickup` — Delivery and pickup
- `customCake` — Custom cake
- `feedbackAndReview` — Feedback and review
- `product` — Product
- `user` — User management, if the group confirms this assignment

The supplied member list assigns delivery/pickup to two members and does not assign user management. Resolve that with the team before relying on the final two branch assignments. Avoid having two people independently push to the same feature branch; coordinate ownership or use separate branches and pull requests.

## 2. Start work from the latest `main`

In IntelliJ, first switch to `main` and pull/update the project. Then create your feature branch from `main` (or switch to the branch if the leader has already created it):

```bash
git switch main
git pull origin main
git switch -c order
```

Replace `order` with your assigned branch. If that branch already exists locally, use `git switch order` instead of `git switch -c order`.

## 3. Commit and push

Before committing, review the changed files and make sure you are not committing passwords, IDE-specific files, generated build files, or unrelated work.

```bash
git add .
git commit -m "Implement order feature"
git push -u origin order
```

For later pushes on that branch:

```bash
git add .
git commit -m "Add order validation"
git push
```

Use short, descriptive commit messages such as `Add product model and repository` or `Implement cake booking update`. Avoid messages like `update`, `done`, or `final`.

## 4. Pull requests and merging

1. Push your branch to GitHub.
2. Open a pull request from your feature branch into `main`.
3. Describe what changed and how you checked it.
4. Ask another member to review it.
5. Resolve any conflicts, then let the project leader merge it.
6. After it is merged, update your local `main` before starting the next task.

Do not push feature work directly to `main`. Avoid force-pushing shared branches unless the team has explicitly coordinated it.

## 5. Code organization

Follow the existing package names and class naming style. In this Spring Boot project, keep responsibilities separate:

- `model`: entities/domain objects and fields.
- `repository`: database persistence.
- `service`: feature logic and validation.
- `controller`: request mappings and HTTP input/output.

Keep database access out of controllers. Use clear class names such as `OrderController`, `OrderService`, `OrderRepository`, and `OrderModel` (or follow the naming already established in the codebase). Make sure packages remain beneath the main application package.

Coordinate shared decisions before implementation, including ID formats, field names, status values, relationships between features, and database configuration. Don’t change a shared interface or configuration without notifying the affected members.

## 6. Database credentials and local configuration

Do not commit real database passwords, API keys, or personal credentials. Keep shared settings free of secrets; configure each developer’s credentials locally through environment variables or an ignored local configuration file. Never commit a local database dump unless the group explicitly decides it is needed and it contains no private data.

## 7. Before requesting review

- Confirm the project compiles and starts locally.
- Test the changed feature and its CRUD operations where implemented.
- Check that new files are included and unrelated files are excluded.
- Pull or merge the latest `main` into your feature branch if needed.
- Explain any incomplete parts in the pull request description.
