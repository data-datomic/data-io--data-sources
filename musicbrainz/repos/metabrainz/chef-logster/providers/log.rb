action :add do
    new_resource.updated_by_last_action(false)

    options = {}
    python_path = ""

    new_resource.prefix && options["--metric-prefix"] = new_resource.prefix
    new_resource.parser_options && options["--parser-options"] = new_resource.parser_options
    new_resource.gmetric_options && options["--gmetric-options"] = new_resource.gmetric_options
    new_resource.graphite_host && options["--graphite-host"] = new_resource.graphite_host
    new_resource.state_dir && options["--state-dir"] = new_resource.state_dir
    new_resource.output && options["--output"] = new_resource.output
    new_resource.python_path && python_path = "PYTHONPATH=" + new_resource.python_path.join(":")

    o = options.map{|k,v| "#{k}=#{v}"}.join(" ")

    directory new_resource.state_dir do
        user "logster"
    end

    cron "logster #{new_resource.log_file.gsub(/\//, '_')}" do
        command "#{python_path} /usr/bin/logster #{o} #{new_resource.parser} #{new_resource.log_file}"
        action :create
        user "logster"
        mailto node['logster']['mailto']
    end
end

action :remove do
    cron "logster #{new_resource.gsub(/\//, '_')}" do
        user "logster"
        action :delete
    end
end
