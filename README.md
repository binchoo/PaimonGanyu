# paimonganyu

## All workflows

### Hoyopass
Every user must securely register his or her Hoyolab credentials(ltuid and ltoken) when first using our system.
![Hoyopass Crud Workflow](https://user-images.githubusercontent.com/15683098/161238482-da48ed4a-dbc8-4312-a017-587979fa547d.png)

### Code redeem
If a user is newly added, the system will try to redeem all known codes for him or her.
![Code Redeem New User Event Workflow](https://user-images.githubusercontent.com/15683098/161238418-30f88f55-cb9f-46f5-a7b5-e7dc97cc02a5.png)

If a new redeem code is registered to the code bucket, this code will soon be redeemed for all users.
![Code Redeem New Redeem Code Workflow](https://user-images.githubusercontent.com/15683098/161238433-a1e7aedb-8696-4b2c-b028-389a2f7bc151.png)

### Daily check-in

If a user is newly added, he or she will soon be checked in to Hoyolab's daily check.
![Daily Check New User Event Workflow](https://user-images.githubusercontent.com/15683098/161238458-4ec1eb9f-50f1-4fdd-afa9-84055ee1aaf4.png)

The system will try to check in all system users three times a day.
![Daily Check Batch Workflow](https://user-images.githubusercontent.com/15683098/161238467-08100825-c5e8-4500-b668-cf7e6fde7228.png)

### Knowing responsibilities among components

To implement and deploy services on the AWS components, relationships among those components must be identified and be configured with the SAM's `template.yaml` file.

![image](https://user-images.githubusercontent.com/15683098/161376846-4eccaccb-e9d3-4ac0-8c0b-28aad5e6c1f1.png)

