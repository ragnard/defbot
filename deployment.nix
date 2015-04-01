{
  network.description = "defbot";
  network.enableRollback = true;

  defbot-server =
    { config, pkgs, ... }:
      let
        defbot = import ./default.nix { inherit pkgs; };

      in {
        networking.firewall.enable = true;
        networking.firewall.allowedTCPPorts = [ 22 ];
        networking.firewall.allowPing = true;
  
        users.extraUsers = {
          defbot = { };
        };

        environment.systemPackages = [
          defbot
        ];

        systemd.services.defbot = {
          description = "defbot";
          after = [ "network.target" ];
          serviceConfig = {
            ExecStart = "${defbot}/bin/defbot " + builtins.readFile(./slack.token);
            User = "defbot";
            Restart = "always";
          };
        };
    };
}

  
