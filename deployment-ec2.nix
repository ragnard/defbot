let
  accessKey = "AKIAJLQHETW2SFQ6TXLQ";
in
{
  defbot-server =
    { resources, ... }:
    {
      deployment.targetEnv = "ec2";
      deployment.ec2.region = "eu-west-1";
      deployment.ec2.instanceType = "t1.micro";
      deployment.ec2.accessKeyId = accessKey;

      deployment.ec2.keyPair = resources.ec2KeyPairs.defbot.name;
      deployment.ec2.securityGroups = [ "allow-ssh" ];
      #  deployment.ec2.subnetId = "subnet-4911973e";
    };

  resources.ec2KeyPairs.defbot = {
    region = "eu-west-1";
    accessKeyId = accessKey;
  };
}


  
