# frozen_string_literal: true

Pod::Spec.new do |s|
  s.name          = 'WordPressMocks'
  s.version       = '0.0.16'

  s.summary       = 'Network mocking for testing the WordPress mobile apps.'
  s.description   = <<-DESC
                    This framework contains a pack of icons – mainly ones used in the WordPress apps –
                    that can be reused and scaled at any resolution.
  DESC

  s.homepage      = 'https://github.com/wordpress-mobile/WordPressMocks'
  s.license       = { type: 'GPLv2', file: 'LICENSE.md' }
  s.author        = { 'The WordPress Mobile Team' => 'mobile@wordpress.org' }

  s.platform      = :ios

  s.source        = { git: 'https://github.com/wordpress-mobile/WordPressMocks.git', tag: s.version.to_s }
  s.preserve_paths = 'WordPressMocks/src', 'scripts'
end
