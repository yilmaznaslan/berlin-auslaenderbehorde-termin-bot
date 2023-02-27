chmod 400 work_mac_os.cer
echo "Creating SSH connectionto aws instance"
ssh -i work_mac_os.cer ec2-user@ec2-44-210-94-136.compute-1.amazonaws.com

