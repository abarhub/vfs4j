import os

def run_cmd(cmd):
	print(f'cmd:{cmd}')
	res=os.system(cmd)
	if res!=0:
		raise Exception('Erreur:'+cmd)

#os.system('mvn --version')

run_cmd('mvn --version')

version='0.7.4'

run_cmd(f'mvn gpg:sign-and-deploy-file -Durl=https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=ossrh -DpomFile=target/vfs4j-parent-{version}.pom -Dfile=target/vfs4j-parent-{version}.pom')

run_cmd(f'mvn gpg:sign-and-deploy-file -Durl=https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=ossrh -DpomFile=core/target/vfs4j-core-{version}.pom -Dfile=core/target/vfs4j-core-{version}.jar')
run_cmd(f'mvn gpg:sign-and-deploy-file -Durl=https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=ossrh -DpomFile=core/target/vfs4j-core-{version}.pom -Dfile=core/target/vfs4j-core-{version}-javadoc.jar -Dclassifier=javadoc')
run_cmd(f'mvn gpg:sign-and-deploy-file -Durl=https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=ossrh -DpomFile=core/target/vfs4j-core-{version}.pom -Dfile=core/target/vfs4j-core-{version}-sources.jar -Dclassifier=sources')

run_cmd(f'mvn gpg:sign-and-deploy-file -Durl=https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=ossrh -DpomFile=tests/target/vfs4j-tests-parent-{version}.pom -Dfile=tests/target/vfs4j-tests-parent-{version}.pom')

run_cmd(f'mvn gpg:sign-and-deploy-file -Durl=https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=ossrh -DpomFile=tests/simple/target/vfs4j-tests-simple-{version}.pom -Dfile=tests/simple/target/vfs4j-tests-simple-{version}.jar')
run_cmd(f'mvn gpg:sign-and-deploy-file -Durl=https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=ossrh -DpomFile=tests/simple/target/vfs4j-tests-simple-{version}.pom -Dfile=tests/simple/target/vfs4j-tests-simple-{version}-javadoc.jar -Dclassifier=javadoc')
run_cmd(f'mvn gpg:sign-and-deploy-file -Durl=https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=ossrh -DpomFile=tests/simple/target/vfs4j-tests-simple-{version}.pom -Dfile=tests/simple/target/vfs4j-tests-simple-{version}-sources.jar -Dclassifier=sources')


run_cmd(f'mvn gpg:sign-and-deploy-file -Durl=https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=ossrh -DpomFile=tests/test_classpath/target/vfs4j-tests-classpath-{version}.pom -Dfile=tests/test_classpath/target/vfs4j-tests-classpath-{version}.jar')
run_cmd(f'mvn gpg:sign-and-deploy-file -Durl=https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/ -DrepositoryId=ossrh -DpomFile=tests/test_classpath/target/vfs4j-tests-classpath-{version}.pom -Dfile=tests/test_classpath/target/vfs4j-tests-classpath-{version}-sources.jar -Dclassifier=sources')
