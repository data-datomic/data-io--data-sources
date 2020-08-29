from locust import HttpLocust, TaskSet, task


class UserBehavior(TaskSet):

    @task()
    def homepage(self):
        self.client.get("/")

    @task()
    def supporters(self):
        self.client.get("/supporters")

    @task()
    def donation(self):
        self.client.get("/donate")

    @task()
    def contact(self):
        self.client.get("/contact")


class WebsiteUser(HttpLocust):
    task_set = UserBehavior
    min_wait = 5000
    max_wait = 9000
