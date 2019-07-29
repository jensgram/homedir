# `lnav` and SFTP

    brew install curl --with-libssh2
    brew install https://raw.githubusercontent.com/Homebrew/homebrew-core/bf663c48bb9a3f052c274d2da4fb2c1b874d05b0/Formula/lnav.rb --with-curl

https://stackoverflow.com/questions/3987683/homebrew-install-specific-version-of-formula

    depends_on "readline"
    depends_on "pcre"
    depends_on "sqlite" if MacOS.version < :sierra
    depends_on "curl" => ["with-libssh2", :optional]

    def install
      # Fix errors such as "use of undeclared identifier 'sqlite3_value_subtype'"
      ENV.delete("SDKROOT")

      args = %W[
        --disable-dependency-tracking
        --prefix=#{prefix}
        --with-readline=#{Formula["readline"].opt_prefix}
      ]

      # macOS ships with libcurl by default, albeit without sftp support. If we
      # want lnav to use the keg-only curl formula that we specify as a
      # dependency, we need to pass in the path.
      args << "--with-libcurl=#{Formula["curl"].opt_lib}" if build.with? "curl"    <---- THIS!!!

      system "./autogen.sh" if build.head?
      system "./configure", *args
      system "make", "install"
    end