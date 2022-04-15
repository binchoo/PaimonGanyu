# paimonganyu

## All workflows

[Currently defined workflows in 2022-04-04](https://github.com/binchoo/paimonganyu/issues/1#issuecomment-1087132930)

## Deployment steps
1. Develop and test domain/infra/application modules.
2. Determine the version of the artifacts. Assign that value to `project.version` variable in `build.gradle`.
3. Change the `CodeUri` property of lambda resources defined in the `template.yaml`. This value should be pointing to `.aws-sam/build/application-${version}.zip` in relative manner.
4. Run command `make` at `paimonganyu` directory, where the `template.yaml` file can be seen.  
   1. Tasks in Makefile begin:
      1. `sam build` analyzes and refactors the `template.yaml`.
      2. In turn, `buildZip` task is triggered, which is defined in`build.gradle` in the root project.
      3. In turn, `copyBuiltZip` task migrates the zip file created by `buildZip` task, into `.aws-sam/build/` directory.
      4. In turn, `sam deploy --guided` occurs to deploy all resources within the owners aws account.
      5. Finally a local test-run begins.

### buildZip and copyBuiltZip tasks
`paimonganyu/PaimonGanyu/build.gradle`
```groovy
    task buildZip(type: Zip) {
        from compileJava
        from processResources
        into('lib') {
            from configurations.runtimeClasspath
        }
    }

    task copyBuiltZip(type: Copy) {
        def pathOut = '../'.repeat(project.depth)
        from buildZip
        into("${pathOut}.aws-sam/build")
    }
```

#### buildZip

In order to deploy a compiled java lambda handler onto AWS lambda service, class itself and all other dependent runtime classpaths should be in one .jar or .zip file.
The `CodeUri` property of lambda resources should point to that file. Using maven with the shade plugin, we may just point to the root project folder, then SAM will transparently find the very right artifact to deploy.
See also, [Deploy Java Lambda functions with .zip or JAR file archives (AWS Docs)](https://docs.aws.amazon.com/lambda/latest/dg/java-package.html#java-package-libraries). 

#### copyBuiltZip

`CodeUri` property of lambdas in `template.yaml` means the place where the relating fat-zip or fat-jar is located. That path is relative to `.aws-sam/build` when not pointing to the root project folder.
Task `copyBuiltZip` moves the artifact which `buildZip` generated into the `.aws-sam/build/` directory. This is to shorten the path string for the `CodeUri`.