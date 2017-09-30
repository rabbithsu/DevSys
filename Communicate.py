from subprocess import Popen, PIPE, STDOUT

dd_process = Popen(['python', 'write.py'], stdout = PIPE)
ssh_process = Popen(['python', 'read.py'], stdin = dd_process.stdout, stdout = PIPE)
dd_process.stdout.close() # enable write error in dd if ssh dies

print 'start'
#ssh_process.communicate()
for line in iter(ssh_process.stdout.readline, b''):
    #XXX run python script here:
    #    `subprocess.call([sys.executable or 'python', '-m', 'your_module'])`
    print line
print 'end'

