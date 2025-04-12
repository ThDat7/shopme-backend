# 👥 Contributing to Shopme

Thank you for your interest in contributing to the Shopme e-commerce platform! This document provides guidelines and instructions for contributing to the project.

## 🤝 Code of Conduct

Please be respectful and considerate of others when contributing to this project. We aim to foster an inclusive and welcoming community.

## 🌟 How to Contribute

There are many ways to contribute to Shopme:

- 🐛 Reporting bugs and issues
- 💡 Suggesting new features or improvements
- 📝 Improving documentation
- 💻 Submitting code changes and bugfixes
- 👀 Reviewing pull requests

## 🚀 Development Workflow

### 1️⃣ Fork the Repository

Create your own fork of the repository on GitHub.

### 2️⃣ Clone Your Fork

```bash
git clone https://github.com/your-username/shopme.git
cd shopme
```

### 3️⃣ Create a Feature Branch

Create a branch for your changes:

```bash
git checkout -b feature/your-feature-name
```

### 4️⃣ Set Up Development Environment

Follow the [💻 Development Guide](5_development.md) to set up your development environment.

### 5️⃣ Make Your Changes

Implement your changes, following the project's coding standards and best practices.

### 6️⃣ Test Your Changes

Ensure your changes work as expected:

```bash
mvn test
```

### 7️⃣ Commit Your Changes

Commit your changes with a clear, descriptive commit message:

```bash
git commit -m "Add feature: your feature description"
```

### 8️⃣ Push to Your Fork

```bash
git push origin feature/your-feature-name
```

### 9️⃣ Create a Pull Request

Create a pull request from your branch to the main Shopme repository's `dev` branch.

## 📋 Pull Request Guidelines

When submitting a pull request:

1. **📝 Describe the Changes**: Provide a clear description of what your pull request does
2. **🔗 Reference Issues**: Link to any related issues (e.g., "Fixes #123")
3. **🎯 Keep it Focused**: Each pull request should address a single concern
4. **✅ Test Coverage**: Ensure your code includes appropriate tests
5. **📚 Documentation**: Update documentation to reflect your changes

## 📏 Code Style and Standards

- Follow Java coding conventions
- Use 4 spaces for indentation (no tabs)
- Write clear, descriptive method and variable names
- Include Javadoc comments for public classes and methods
- Keep methods concise and focused on a single responsibility
- Use proper null handling and exception management

## 📨 Commit Message Format

Use clear, descriptive commit messages:

```
[Type]: Short summary (max 50 chars)

More detailed explanatory text, if necessary. Wrap it to about 72
characters. The blank line separating the summary from the body is
critical.

- Bullet points are acceptable
- Use a hyphen or asterisk followed by a space

Resolves: #123
See also: #456, #789
```

Where `[Type]` is one of:
- `Feature`: New feature
- `Fix`: Bug fix
- `Docs`: Documentation changes
- `Style`: Code style changes (formatting, etc.)
- `Refactor`: Code refactoring
- `Test`: Test additions or changes
- `Chore`: Build process or tool changes

## 🐛 Reporting Bugs

When reporting bugs, please include:

1. **🔍 Bug Description**: Clear, concise description of the issue
2. **📋 Steps to Reproduce**: Detailed steps to reproduce the bug
3. **⚡ Expected Behavior**: What you expected to happen
4. **❌ Actual Behavior**: What actually happened
5. **🖥️ Environment**: Your environment details (OS, Java version, etc.)
6. **📸 Screenshots**: If applicable

## 💡 Feature Requests

When suggesting a new feature:

1. **📝 Feature Description**: Clear description of the feature
2. **🔄 Use Case**: Explain the use case or problem this feature solves
3. **🚀 Implementation Ideas**: If you have thoughts on implementation, include them

## ⚖️ License

By contributing to this project, you agree that your contributions will be licensed under the project's [Apache License 2.0](../LICENSE).

## ❓ Questions?

If you have any questions about contributing, feel free to open an issue for discussion.

## ⏭️ What's Next

Thank you for your contributions to the Shopme platform! Together, we can make this e-commerce solution even better.

Return to [🛒 Documentation Home](README.md)
