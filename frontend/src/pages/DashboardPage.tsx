import { useAuthStore } from "../store/authStore";

function DashboardPage() {
  const { user } = useAuthStore();

  return (
    <main className="max-w-4xl mx-auto py-8">
      <article className="bg-white rounded-lg shadow-md p-8">
        <header>
          <h1 className="text-3xl font-bold mb-8 text-center">대시보드</h1>
        </header>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <section
            className="bg-primary-50 p-6 rounded-lg"
            aria-label="환영 메시지"
          >
            <h2 className="text-xl font-semibold text-primary-700 mb-4">
              <span role="img" aria-label="웃는 이모지">
                😊
              </span>{" "}
              환영합니다!
            </h2>
            <p className="text-gray-600">
              안녕하세요,{" "}
              <strong className="font-medium text-primary-600">
                {user?.name}({user?.nickname})
              </strong>
              님!
            </p>
          </section>

          <section className="bg-gray-50 p-6 rounded-lg" aria-label="계정 정보">
            <h2 className="text-xl font-semibold text-gray-700 mb-4">
              <span role="img" aria-label="클립보드 이모지">
                📋
              </span>{" "}
              계정 정보
            </h2>
            <dl className="space-y-2 text-sm">
              <div>
                <dt className="font-bold inline">이메일: </dt>
                <dd className="inline">{user?.email}</dd>
              </div>
              <div>
                <dt className="font-bold inline">이름: </dt>
                <dd className="inline">{user?.name}</dd>
              </div>
              <div>
                <dt className="font-bold inline">닉네임: </dt>
                <dd className="inline">{user?.nickname}</dd>
              </div>
              <div>
                <dt className="font-bold inline">전화번호: </dt>
                <dd className="inline">{user?.phoneNumber}</dd>
              </div>
              <div>
                <dt className="font-bold inline">가입일: </dt>
                <dd className="inline">
                  {user?.createdAt ? (
                    <time dateTime={user.createdAt}>
                      {new Date(user.createdAt).toLocaleDateString()}
                    </time>
                  ) : (
                    "-"
                  )}
                </dd>
              </div>
            </dl>
          </section>
        </div>
      </article>
    </main>
  );
}

export default DashboardPage;
