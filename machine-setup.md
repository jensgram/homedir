# Machine setup

1. Install Homebrew: https://brew.sh/
2. Install Oh My ZSH: https://ohmyz.sh/
3. `brew install curl git lnav tig unrar wget zsh jenv`
4. `brew cask install dropbox boostnote kdiff3 postman slack spectacle sublime-text vlc jetbrains-toolbox licecap gimp firefox sonos thunderbird`

## Oh My ZSH

`~/.zshrc`:

    export ZSH=/Users/jensgram/.oh-my-zsh
    ZSH_THEME="re5et"
    COMPLETION_WAITING_DOTS="true"
    HIST_STAMPS="yyyy-mm-dd"
    plugins=(git wd brew brew-cask)
    
`themes/re5et.zsh-theme` diff:

    -RPS1='${return_code} %D - %*'
    +RPS1='${return_code} %D{%d-%m-%Y} - %*'

## jEnv – Multiple Java versions

Install desired JDKs:

    % brew cask install adoptopenjdk11
    % …

Add to `~/.zshrc`:

    # jenv
    export PATH="$HOME/.jenv/bin:$PATH"
    eval "$(jenv init -)"

Then, set global vesion:

    % jenv enable-plugin export
    % jenv enable-plugin maven
    % jenv global 11.0

## Global `.gitignore`

`~/.gitignore_global`:

    # OS specific
    .DS_Store

    # IDE: IntelliJ
    .idea/
    *.iml

    # jEnv per-directory Java version
    .java-version

Then, add as global default:

    % git config --global core.excludesfile ~/.gitignore_global

## Spectacle

    > cat Library/Application\ Support/Spectacle/Shortcuts.json
    [
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "RedoLastMove"
      },
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "MakeSmaller"
      },
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "MoveToUpperLeft"
      },
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "MoveToUpperRight"
      },
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "MoveToBottomHalf"
      },
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "MoveToNextDisplay"
      },
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "MoveToTopHalf"
      },
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "MoveToLowerLeft"
      },
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "MakeLarger"
      },
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "UndoLastMove"
      },
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "MoveToPreviousDisplay"
      },
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "MoveToFullscreen"
      },
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "MoveToNextThird"
      },
      {
        "shortcut_key_binding" : "alt+cmd+left",
        "shortcut_name" : "MoveToLeftHalf"
      },
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "MoveToCenter"
      },
      {
        "shortcut_key_binding" : "alt+cmd+right",
        "shortcut_name" : "MoveToRightHalf"
      },
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "MoveToPreviousThird"
      },
      {
        "shortcut_key_binding" : null,
        "shortcut_name" : "MoveToLowerRight"
      }
    ]
