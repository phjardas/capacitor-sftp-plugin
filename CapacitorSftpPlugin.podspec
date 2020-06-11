
  Pod::Spec.new do |s|
    s.name = 'CapacitorSftpPlugin'
    s.version = '0.0.1'
    s.summary = 'Provides upload and download functionality via SFTP'
    s.license = 'MIT'
    s.homepage = 'httphttps://github.com/phjardas/capacitor-sftp-plugin'
    s.author = 'Philipp Jardas <philipp@jardas.de>'
    s.source = { :git => 'httphttps://github.com/phjardas/capacitor-sftp-plugin', :tag => s.version.to_s }
    s.source_files = 'ios/Plugin/**/*.{swift,h,m,c,cc,mm,cpp}'
    s.ios.deployment_target  = '11.0'
    s.dependency 'Capacitor'
  end