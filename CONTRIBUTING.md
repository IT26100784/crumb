# Contributing to Crumb

Thank you for contributing to **Crumb – Bakery & Custom Cake Booking Platform**. This guide explains how team members can work on the project, manage Git branches, and submit changes safely.

## Team Members and Branch Allocation

Each team member is responsible for their assigned component and should work on their own feature branch.

| No. | Team Member       | Assigned Component | Git Branch |
|---|-------------------|---|---|
| 1 | Rathnayake RMPR   | Customer Management | `feature/customer` |
| 2 | Rushdi FR         | Product Management | `feature/product` |
| 3 | Kumara TGVSS      | Custom Cake Management | `feature/custom-cake` |
| 4 | Wettasinghe KKDST | Order Management | `feature/order` |
| 5 | Dilhara KBOU      | Booking Management | `feature/booking` |
| 6 | Munasinghe DA     | Payment and Reviews | `feature/payment-review` |

Replace the placeholder names with the actual team members' names. Make sure the branch names match those created on GitHub.

### Shared Branches

- `main` — Stable, finalized version of the project.
- `develop` — Integration branch for completed features and testing.
- `feature/*` — Individual team members' development branches.

## 1. Before You Start

Make sure you have:

- Access to the Crumb GitHub repository.
- Java 17 installed.
- MySQL 8.0 or later installed.
- IntelliJ IDEA installed.
- Read the setup instructions in [`README.md`](README.md).

Check with the team before modifying another member's assigned component.

## 2. Get the Project

### Using IntelliJ IDEA GUI

1. Open IntelliJ IDEA.
2. Select **File → New → Project from Version Control**.
3. Select Git and enter the GitHub repository URL.
4. Choose a local directory.
5. Click **Clone**.
6. Wait for IntelliJ IDEA to import the Maven project and download dependencies.

### Using the Terminal

```bash
git clone <repository-url>
cd <project-folder>
```

Replace the placeholders with your actual repository URL and project folder.

## 3. Create Your Feature Branch

Do not develop directly on `main` or `develop`.

### Using IntelliJ IDEA GUI

1. Open the project.
2. Click the current branch name in the bottom-right corner.
3. Select **New Branch**.
4. Enter your assigned branch name, such as `feature/customer`.
5. Confirm to create and switch to the branch.

### Using the Terminal

First, update your local branch information:

```bash
git fetch origin
git switch main
git pull origin main
```

Create your assigned branch:

```bash
git switch -c feature/your-feature
```

For example:

```bash
git switch -c feature/customer
```

If the branch already exists on GitHub, use:

```bash
git fetch origin
git switch --track origin/feature/customer
```

Replace `feature/customer` with your assigned branch.

## 4. Develop and Test

1. Work on your assigned component.
2. Follow the existing Java package structure and coding style.
3. Test your changes before committing.
4. Check that the application builds and runs.
5. Review your modified files before committing.
6. Do not commit passwords, API keys, personal IDE settings, or machine-specific paths.

## 5. Commit Your Changes

Use clear and descriptive commit messages.

Examples:

- `Add product search`
- `Fix booking validation`
- `Implement customer registration`
- `Update contribution guidelines`

### Using IntelliJ IDEA GUI

1. Open the **Commit** tool window.
2. Review the list of changed files.
3. Select only the files you intend to commit.
4. Enter a meaningful commit message.
5. Click **Commit**.

### Using the Terminal

Check your changes:

```bash
git status
```

Stage the relevant files:

```bash
git add <file-name>
```

Or stage all intended changes:

```bash
git add .
```

Commit your changes:

```bash
git commit -m "Describe your changes"
```

Review the staged files carefully before using `git add .`.

## 6. Push Your Feature Branch

### Using IntelliJ IDEA GUI

1. Select **Git → Push**.
2. Review the branch and commits.
3. Click **Push**.

### Using the Terminal

For the first push:

```bash
git push -u origin feature/your-feature
```

For subsequent pushes:

```bash
git push
```

Replace the branch name with your assigned feature branch.

## 7. Create a Pull Request

After pushing your changes:

1. Open the Crumb repository on GitHub.
2. Select **Pull requests → New pull request**.
3. Select your feature branch as the source branch.
4. Select `develop` as the target branch.
5. Describe your changes and explain how you tested them.
6. Create the pull request.
7. Ask a teammate to review your work.
8. Resolve any requested changes before merging.

Only merge after the changes have been reviewed and tested.

## 8. Keep Your Feature Branch Updated

If the team uses `develop` as its integration branch, you may need to merge its latest changes into your feature branch.

First, update `develop`:

```bash
git fetch origin
git switch develop
git pull origin develop
```

Then update your feature branch:

```bash
git switch feature/your-feature
git merge develop
```

Resolve any merge conflicts carefully and test the application again.

Coordinate with your team before resolving conflicts in shared code.

## 9. Recommended Team Workflow

Follow this workflow for each feature:

1. Create or switch to your assigned feature branch.
2. Implement and test your changes.
3. Commit and push your changes.
4. Create a pull request into `develop`.
5. Review and test the changes.
6. Merge the approved pull request into `develop`.
7. Test the integrated application.
8. Merge `develop` into `main` when the project is stable and ready for release.

If the team agrees on a different workflow, follow the agreed approach.

## 10. Pull Request Checklist

Before submitting a pull request, confirm that:

- [ ] I am working on the correct feature branch.
- [ ] My changes are related to my assigned task.
- [ ] The application builds and runs.
- [ ] I tested the relevant functionality.
- [ ] I did not commit passwords or other secrets.
- [ ] I updated documentation if necessary.
- [ ] I explained my changes in the pull request.
- [ ] I checked for potential conflicts with other team members' work.

## 11. Important Git Safety Rules

- Do not commit directly to `main` unless the team explicitly agrees.
- Do not force-push to shared branches such as `main` or `develop`.
- Do not rewrite shared Git history without coordinating with the team.
- Do not use destructive commands such as `git reset --hard` unless you understand their effects.
- Pull and review changes before working with shared branches.
- Keep commits focused and communicate with teammates when changing shared code.
- Never commit database passwords, API keys, or other confidential information.

Thank you for helping build Crumb!
