# Contributing

All Contributions to this repository start with a Jira Item associated with the work request.

## Pull Request Process

1. Fork the repository on you personal github space. see:[working with forks](https://help.github.com/en/github/collaborating-with-issues-and-pull-requests/working-with-forks).
1. [Build](README.md#Run) and run the application on your local environment.
1. Commit your changes to your branch.
1. Update the README.md with details of changes to the interface, this includes new environment 
   variables, exposed ports, useful file locations and container parameters.
1. When your change is ready to be reviewed, open a PR on this repository using our [Pull Request Template](.github/PULL_REQUEST_TEMPLATE) and optionally assign one or more reviewer. Also, in the comment of the Jira item, put a link to the Pull Request and move the item to `in review`
1. If some changes are required, you will be notified in the PR, address any change requested and push to the same branch.
1. When your change is meeting the requirement, the reviewer will merge the code into master, you can celebrate! 

## Open a new bug in jira

- When a bug is detected in the application, a jira bug must be open
    the bug should be documented as followed:
        - Expected Behavior
        - Current Behavior
        - Steps to Reproduce
        - Environment
        - Description (stack traces, screenshot, error messages, splunk logs query)
        - Optionally you can suggest a possible solution
    - set the target release

## Open a new feature request in jira

- For feature enhancements, a jira task or story must be open
    - set the target release
