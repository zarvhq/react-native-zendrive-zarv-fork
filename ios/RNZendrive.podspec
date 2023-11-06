Pod::Spec.new do |s|
  s.name         = "RNZendrive"
  s.version      = "6.2.1"
  s.summary      = "Zendrive"
  s.description  = "React Native bridge module wrapper around Zendrive SDK"
  s.homepage     = "www.zendrive.com"
  s.license      = "MIT"
  s.author             = { "Saunak" => "saunak@zendrive.com" }
  s.ios.deployment_target = "12.4"
  s.source       = { :path => '.' }
  s.source_files  = "RNZendrive/Source/**/**"
  s.swift_version = '5.1'
  s.static_framework = true
  s.pod_target_xcconfig = { 'ENABLE_BITCODE' => 'NO' }
  s.dependency 'React'
  s.dependency 'ZendriveSDK'
end
