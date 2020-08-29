from locust import HttpLocust, TaskSet, task


class UserBehavior(TaskSet):

    @task()
    def homepage(self):
        self.client.get("/")

    @task()
    def about(self):
        self.client.get("/about")

    @task()
    def log(self):
        self.client.get("/log")

    @task()
    def review_browsing(self):
        for i in range(1, 330):
            # Statistics for these requests will be grouped under: /review/?page=[i]
            self.client.get("/review/?page=%i" % i, name="/review/?page=[i]")


class WebsiteUser(HttpLocust):
    task_set = UserBehavior
    min_wait = 5000
    max_wait = 9000
