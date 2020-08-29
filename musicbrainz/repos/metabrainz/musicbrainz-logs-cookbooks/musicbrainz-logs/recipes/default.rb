# setup the logstash directory

directory "/usr/local/logstash" do
  owner "root"
  group "root"
  mode 0755
  action :create
end

directory "/usr/local/logstash/jar" do
  owner "root"
  group "root"
  mode 0755
  action :create
end

directory "/usr/local/logstash/conf" do
  owner "root"
  group "root"
  mode 0755
  action :create
end

remote_file "/usr/local/logstash/jar/logstash-1.3.2-flatjar.jar" do
  source "https://download.elasticsearch.org/logstash/logstash/logstash-1.3.2-flatjar.jar"
  mode 0644
  action :create_if_missing
end

link "/usr/local/logstash/jar/logstash.jar" do
  to "/usr/local/logstash/jar/logstash-1.3.2-flatjar.jar"
end

cookbook_file "/usr/local/logstash/conf/indexer.conf" do
  source "indexer.conf"
  group "root"
  owner "root"
  mode "0755"
end

cookbook_file "/usr/local/logstash/conf/shipper.conf" do
  source "shipper.conf"
  group "root"
  owner "root"
  mode "0755"
end

# Create the logstash-indexer service

directory "/usr/local/logstash-indexer" do
  owner "root"
  group "root"
  mode 0755
  action :create
end

directory "/usr/local/logstash-indexer/log" do
  owner "root"
  group "root"
  mode 0755
  action :create
end

link "/usr/local/logstash-indexer/logstash" do
  to "/usr/local/logstash"
end

cookbook_file "/usr/local/logstash-indexer/run" do
  source "logstash-indexer-run"
  group "root"
  owner "root"
  mode "0755"
end

cookbook_file "/usr/local/logstash-indexer/log/run" do
  source "logstash-indexer-log-run"
  group "root"
  owner "root"
  mode "0755"
end

# Create the logstash-shipper service

directory "/usr/local/logstash-shipper" do
  owner "root"
  group "root"
  mode 0755
  action :create
end

directory "/usr/local/logstash-shipper/log" do
  owner "root"
  group "root"
  mode 0755
  action :create
end

link "/usr/local/logstash-shipper/logstash" do
  to "/usr/local/logstash"
end

cookbook_file "/usr/local/logstash-shipper/run" do
  source "logstash-shipper-run"
  group "root"
  owner "root"
  mode "0755"
end

cookbook_file "/usr/local/logstash-shipper/log/run" do
  source "logstash-indexer-log-run"
  group "root"
  owner "root"
  mode "0755"
end

# setup the musicbrainz-logs directory
directory "/usr/local/musicbrainz-logs" do
  owner "root"
  group "root"
  mode 0755
  action :create
end

# start various services

service "redis" do
  action :start
end

link "/etc/service/logstash-indexer" do
  to "/usr/local/logstash-indexer"
end

link "/etc/service/logstash-shipper" do
  to "/usr/local/logstash-shipper"
end

service "svscan" do
  action :start
end
