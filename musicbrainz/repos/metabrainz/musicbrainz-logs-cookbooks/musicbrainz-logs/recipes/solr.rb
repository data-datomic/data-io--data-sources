# download a more current version of solr, than what is in packages
directory "/usr/local/solr-dist" do
  owner "root"
  group "root"
  mode 0755
  action :create
end

#remote_file "http://www.us.apache.org/dist/lucene/solr/4.6.0/solr-4.6.0.tgz" do
remote_file "/usr/local/solr-dist/solr-4.6.0.tgz" do
  source "http://ftp.cixug.es/apache/lucene/solr/4.6.0/solr-4.6.0.tgz"
  mode 00644
  action :create_if_missing
end

tar_extract "file:///usr/local/solr-dist/solr-4.6.0.tgz" do
  target_dir "/usr/local"
  creates "/usr/local/solr-4.6.0"
end

link "/usr/local/solr" do
  to "/usr/local/solr-4.6.0"
end

bash "save solr default config" do
  code <<-EOL
  mv /usr/local/solr/example/solr /usr/local/solr/example/solr-default-config
  EOL
  not_if do ::File.exists?('/usr/local/solr/example/solr-default-config') end
end

bash "create solr data dir in /var/solr" do
  code <<-EOL
  cp -r /usr/local/solr/example/example-schemaless/solr /var
  EOL
  not_if do ::File.exists?('/var/solr') end
end

link "/usr/local/solr/example/solr" do
  to "/var/solr"
end

cookbook_file "/var/solr/collection1/conf/solrconfig.xml" do
  source "solrconfig.xml"
  group "root"
  owner "root"
  mode "0755"
end

cookbook_file "/var/solr/collection1/conf/schema.xml" do
  source "schema.xml"
  group "root"
  owner "root"
  mode "0755"
end

directory "/usr/local/solr-service" do
  owner "root"
  group "root"
  mode 0755
  action :create
end

directory "/usr/local/solr-service/log" do
  owner "root"
  group "root"
  mode 0755
  action :create
end

link "/usr/local/solr-service/solr" do
  to "/usr/local/solr"
end

cookbook_file "/usr/local/solr-service/run" do
  source "solr-run"
  group "root"
  owner "root"
  mode "0755"
end

cookbook_file "/usr/local/solr-service/log/run" do
  source "logstash-indexer-log-run"
  group "root"
  owner "root"
  mode "0755"
end

link "/etc/service/solr-service" do
  to "/usr/local/solr-service"
end
