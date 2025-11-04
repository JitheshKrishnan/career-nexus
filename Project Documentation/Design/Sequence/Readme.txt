 1. Authentication & User Management (3 diagrams)

User Registration & Email Verification

Job Seeker registers → Email verification → Account activation
Actors: Job Seeker, Gateway, User Service, Notification Service, Email Provider


User Login & JWT Token Generation

Login → Validate credentials → Generate JWT (access + refresh tokens)
Actors: Job Seeker, Gateway, User Service, JWT Service


Password Reset Flow

Forgot password → Email reset link → Validate token → Reset password
Actors: Job Seeker, Gateway, User Service, Notification Service




📄 2. Resume Management (2-3 diagrams)

Resume Upload & Parsing

Upload resume → Store in S3 → Extract text (Tika) → NLP parsing → Store parsed data
Actors: Job Seeker, Gateway, Resume Service, S3, NLP/AI Service, PostgreSQL


Resume Analysis & Quality Score

Get resume → Analyze completeness → Calculate quality score → Return insights
Actors: Job Seeker, Gateway, Resume Service, NLP/AI Service


Skills Extraction from Resume (Optional - can merge with #4)

Resume text → NLP Service → NER extraction → Skills normalization → Store
Actors: Resume Service, NLP/AI Service, PostgreSQL




💼 3. Job Matching & Recommendations (2-3 diagrams)

Job Search & Filtering

Search jobs by keyword/location → Query Elasticsearch → Filter results → Return jobs
Actors: Job Seeker, Gateway, Job Matcher Service, Elasticsearch, External Job APIs


Compute Job Match Score

Get resume skills → Get job requirements → Calculate match score → Return score + gaps
Actors: Job Seeker, Gateway, Job Matcher Service, Resume Service, NLP/AI Service


Get Personalized Job Recommendations

Fetch user profile → Get resume → Match against jobs → Rank by score → Cache → Return top matches
Actors: Job Seeker, Gateway, Job Matcher Service, Resume Service, NLP/AI Service, Redis




📚 4. Learning & Skill Development (3-4 diagrams)

Skill Gap Analysis

Get resume skills → Get target job/role → Identify gaps → Prioritize → Return analysis
Actors: Job Seeker, Gateway, Learning Suggester Service, Resume Service, Job Matcher Service


Course Recommendations (Multi-source Aggregation)

Skill gap analysis → Query Udemy API → Query Coursera API → Query YouTube API → Aggregate → Rank → Cache → Return courses
Actors: Job Seeker, Gateway, Learning Suggester Service, Udemy, Coursera, YouTube, Redis


Generate Learning Path

Skill gap analysis → Resolve dependencies → Order by priority → Assign courses → Create milestones → Store path → Return
Actors: Job Seeker, Gateway, Learning Suggester Service, Course APIs, PostgreSQL


Track Learning Progress & Update Skills (Optional)

Mark course complete → Update progress → Mark skill acquired → Update resume → Notify
Actors: Job Seeker, Gateway, Learning Suggester Service, Resume Service, Notification Service




🔔 5. Notifications (1-2 diagrams)

Send Notification (Email & Push)

Event triggered → Queue notification → Process template → Check preferences → Send via SendGrid/FCM → Log
Actors: Any Service, Notification Service, Kafka/RabbitMQ, SendGrid, FCM, PostgreSQL


Job Match Alert Notification (Optional - can be part of #14)

Job match found → Notification Service → Build email → Send → Track delivery
Actors: Job Matcher Service, Notification Service, SendGrid




🛡️ 6. Gateway & Cross-cutting Concerns (2 diagrams)

API Gateway Request Flow (with Auth, Rate Limiting, Circuit Breaker)

Request → JWT validation → Rate limit check → Route to service → Circuit breaker → Response
Actors: Client, Gateway, Service Discovery, Redis, Target Microservice


Error Handling & Fallback Response

Service failure → Circuit breaker opens → Return cached/fallback response → Log error
Actors: Gateway, Target Service, Redis, Monitoring